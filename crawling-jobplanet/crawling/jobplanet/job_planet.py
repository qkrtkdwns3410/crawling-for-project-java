import gc
import logging
import os
from urllib.parse import unquote

import psutil  # 메모리 모니터링을 위한 라이브러리
from dotenv import load_dotenv
from selenium import webdriver
from selenium.common import TimeoutException, WebDriverException
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager

from crawling.file_util import file_util
from crawling.jobplanet.company_info import CompanyInfo

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# .env 파일 로드
load_dotenv()

# 이메일 비밀번호 호출
EMAIL = os.getenv('EMAIL')
PASSWORD = os.getenv('PASSWORD')

logging.info(f'EMAIL, PASSWORD: {EMAIL}, {PASSWORD}')

# Chrome Option 설정
options = webdriver.ChromeOptions()
# options.add_argument('--headless')
options.add_argument('--ignore-ssl-errors=yes')
options.add_argument('--ignore-certificate-errors')
options.add_argument('--disable-dev-shm-usage')
options.add_argument('--disable-extensions')
options.add_argument('--window-size=640,480')
options.add_argument('--no-sandbox')
options.add_argument('--log-level=3')
options.add_argument('--disable-gpu')
options.add_argument('--incognito')
# options.add_argument('--disable-images')
options.add_experimental_option("prefs", {'profile.managed_default_content_settings.images': 2})
options.add_argument('--blink-settings=imagesEnabled=false')


def start_driver(options):
    driver_path = ChromeDriverManager().install()
    chrome_driver_path = os.path.join(os.path.dirname(driver_path), "chromedriver.exe")
    # 옵션 추가
    # WebDriver 초기화 시 service 매개변수 사용
    return webdriver.Chrome(service=Service(executable_path=chrome_driver_path), options=options)


def restart_driver(driver, options):
    try:
        driver.quit()
    except Exception as e:
        logging.error(f"Error quitting driver: {e}")
    gc.collect()
    logging.info("Driver restarted to free memory.")
    return start_driver(options)


def convert_url_to_decoded_url(current_url):
    decoded_url = unquote(current_url)
    return decoded_url


def parse_company_name(url):
    _c_url = url.split('/')[-1]
    _c_url = _c_url.split('?')[0]
    return _c_url


def remove_company_info_if_exist():
    if os.path.exists('company_data.json'):
        os.remove('company_data.json')


def check_memory_usage(threshold_mb=500):
    process = psutil.Process(os.getpid())
    mem = process.memory_info().rss / (1024 * 1024)  # MB 단위

    logging.debug(f"Current memory usage: {mem} MB")

    return mem > threshold_mb


# Service 객체 생성
driver = start_driver(options)


def restart_driver_if_close_to_oom():
    global driver
    if check_memory_usage(threshold_mb=500):  # 임계값 설정 (예: 500MB)
        driver = restart_driver(driver, options)
        # 현재 페이지 다시 로드
        driver.get(driver.current_url)
        logging.info(f"Driver restarted. Reloaded current page: {driver.current_url}")


try:
    # 페이지 이동
    driver.get("https://www.jobplanet.co.kr/companies?page=1")

    ## 존재하는 company_data.json 파일이 있다면 삭제
    remove_company_info_if_exist()

    while True:  # 페이지 끝까지 순회
        # 현재 페이지에서 섹션을 순회
        # 현재 페이지 로깅
        logging.info(f"Current Page: {driver.current_url}")

        for section_index in range(1, 11):  # 섹션은 1부터 시작
            try:
                # 섹션 내부 a 태그 추출
                section_xpath = f"//div[@class='section_group']/section[{section_index}]/div/div/dl[1]/dt/a"
                element = WebDriverWait(driver, 3).until(ec.presence_of_element_located((By.XPATH, section_xpath)))

                # 데이터 출력
                logging.info(f"Section {section_index}: {element.text}")
                inner_link = element.get_attribute('href')
                converted_url = convert_url_to_decoded_url(inner_link)

                # 회사 이름 추출 converted_url 의 마지막을 가져온다
                company_name = parse_company_name(converted_url)

                # 별점 점수
                rating_element = WebDriverWait(driver, 5).until(
                    ec.presence_of_element_located((By.XPATH, f"/html/body/div[1]/div[2]/div/div[1]/div[1]/article/div/div[1]/section[{section_index}]/div/div/dl[2]/dd[1]/span")))

                ## 별점 개수
                rating_count_element = WebDriverWait(driver, 5).until(
                    ec.presence_of_element_located((By.XPATH, f"/html/body/div[1]/div[2]/div/div[1]/div[1]/article/div/div[1]/section[{section_index}]/div/div/dl[2]/dt")), "개의")

                rating_count = rating_count_element.text.split("개의")[0]

                # int 값으로 변경하고 1,140 -> 1140
                rating_count = int(rating_count.replace(",", ""))

                # 데이터 저장
                # JSON 파일에 저장
                company_info = CompanyInfo.from_data(company_name, rating_element.text, rating_count, converted_url).to_dict()
                file_util.save_to_json_line(company_info, 'company_data.jsonl')

                logging.info(f"Rating: {rating_element.text}")
                logging.info(f"Rating Count: {rating_count}")

                logging.info(f"Link: {converted_url}")
                logging.info(f"Company Name: {company_name}")

                # 메모리 사용량 체크 및 필요 시 WebDriver 재시작
                restart_driver_if_close_to_oom()
            except TimeoutException:
                logging.info(f"Section {section_index}: Not found. Moving to next page if available.")
                break  # 섹션이 없으면 다음 페이지로 이동
            except WebDriverException as e:
                logging.error(f"WebDriverException encountered: {e}. Attempting to restart driver.")
                driver = restart_driver(driver, options)
                driver.get(driver.current_url)
                continue
            except Exception as e:
                logging.error(f"Error parsing section {section_index}: {e}")
                continue

        # 다음 페이지 버튼 확인 및 클릭
        try:
            # 이전 URL 저장
            _previous_url = driver.current_url

            # 다음 버튼 클릭 대기 및 클릭
            next_page_button = WebDriverWait(driver, 10).until(ec.element_to_be_clickable((By.XPATH, "//a[@class='btn_pgnext']")))
            next_page_button.click()
            logging.info("Next page button clicked. Waiting for the next page to load...")

            # 페이지 로딩을 위해 잠시 대기
            WebDriverWait(driver, 10).until(lambda d: d.current_url != _previous_url)

            logging.info("Page URL changed successfully.")
        except TimeoutException:
            logging.info("Next page button not found or page did not load. Stopping.")
            break

finally:
    driver.quit()
