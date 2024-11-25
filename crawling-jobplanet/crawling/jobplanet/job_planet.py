import json
import logging
import os
from urllib.parse import unquote

from dotenv import load_dotenv
from selenium import webdriver
from selenium.common import TimeoutException
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager

from crawling.jobplanet.company_info import CompanyInfo

# 로깅 설정
logger = logging.getLogger(__name__)

# .env 파일 로드
load_dotenv()

# 이메일 비밀번호 호출
EMAIL = os.getenv('EMAIL')
PASSWORD = os.getenv('PASSWORD')

logger.info(f'EMAIL, PASSWORD: {EMAIL}, {PASSWORD}')

# Chrome Option 설정
options = webdriver.ChromeOptions()
# options.add_argument('--headless')
options.add_argument('--ignore-ssl-errors=yes')
options.add_argument('--ignore-certificate-errors')
options.add_argument('--disable-dev-shm-usage')
options.add_argument('--disable-extensions')
options.add_argument('--window-size= 640 x 480')
options.add_argument('--no-sandbox')
options.add_argument('--log-level=3')
options.add_argument('--disable-gpu')
options.add_argument('--incognito')
options.add_argument('--disable-images')
options.add_experimental_option("prefs", {'profile.managed_default_content_settings.images': 2})
options.add_argument('--blink-settings=imagesEnabled=false')

# Service 객체 생성
driver_path = ChromeDriverManager().install()
chrome_driver_path = os.path.join(os.path.dirname(driver_path), "chromedriver.exe")

# 옵션 추가

# WebDriver 초기화 시 service 매개변수 사용
driver = webdriver.Chrome(service=Service(executable_path=chrome_driver_path), options=options)

# 잡플래닛 로그인 페이지 이동
# driver.get("https://www.jobplanet.co.kr/users/sign_in?_nav=gb")

# # 이메일 입력 <input data-valid="false" id="user_email" name="user[email]" placeholder="이메일 주소" type="email" value="qkrtkdwns3410@naver.com">
# email_input = driver.find_element(By.ID, "user_email")
#
# # 비밀번호 입력
# password_input = driver.find_element(By.ID, "user_password")
#
# # 로그인 버튼 클릭 <button class="btn_sign_up" type="submit">이메일로 로그인</button>
# login_button = driver.find_element(By.CLASS_NAME, "btn_sign_up")
#
# # 로그인 버튼 클릭
# email_input.send_keys(EMAIL)
# password_input.send_keys(PASSWORD)
# login_button.click()

def convert_url_to_decoded_url(current_url):
    decoded_url = unquote(current_url)
    return decoded_url


def parse_company_name(url):
    _c_url = url.split('/')[-1]
    _c_url = _c_url.split('?')[0]
    return _c_url

def save_to_json(data, filename='company_data.json'):
    # 파일이 비어 있는지 확인
    if os.path.exists(filename) and os.path.getsize(filename) > 0:
        with open(filename, 'r+', encoding='utf-8') as f:
            f.seek(0, os.SEEK_END)
            f.seek(f.tell() - 1, os.SEEK_SET)
            f.truncate()
            f.write(',')
            json.dump(data, f, ensure_ascii=False, separators=(',', ':'))
            f.write(']')
    else:
        with open(filename, 'w', encoding='utf-8') as f:
            f.write('[')
            json.dump(data, f, ensure_ascii=False, separators=(',', ':'))
            f.write(']')


def remove_company_info_if_exist():
    if os.path.exists('company_data.json'):
        os.remove('company_data.json')


try:
    # 페이지 이동
    driver.get("https://www.jobplanet.co.kr/companies/by_industry/700?page=1")

    ## 존재하는 company_data.json 파일이 있다면 삭제
    remove_company_info_if_exist()

    while True:  # 페이지 끝까지 순회
        # 현재 페이지에서 섹션을 순회
        for section_index in range(1, 11):  # 섹션은 1부터 시작
            try:
                # 섹션 내부 a 태그 추출
                section_xpath = f"//div[@class='section_group']/section[{section_index}]/div/div/dl[1]/dt/a"
                element = WebDriverWait(driver, 3).until(
                    ec.presence_of_element_located((By.XPATH, section_xpath))
                )

                # 데이터 출력
                print(f"Section {section_index}: {element.text}")
                inner_link = element.get_attribute('href')
                converted_url = convert_url_to_decoded_url(inner_link)

                # 회사 이름 추출 converted_url 의 마지막을 가져온다
                company_name = parse_company_name(converted_url)

                # 별점 점수
                rating_element = WebDriverWait(driver, 5).until(
                    ec.presence_of_element_located(
                        (By.XPATH, f"/html/body/div[1]/div[2]/div/div[1]/div[1]/article/div/div[1]/section[{section_index}]/div/div/dl[2]/dd[1]/span"))
                )

                ## 별점 개수
                rating_count_element = WebDriverWait(driver, 5).until(
                    ec.presence_of_element_located(
                        (By.XPATH, f"/html/body/div[1]/div[2]/div/div[1]/div[1]/article/div/div[1]/section[{section_index}]/div/div/dl[2]/dt"))
                )

                rating_count = rating_count_element.text.split("개의")[0]

                # int 값으로 변경하고 1,140 -> 1140
                rating_count = int(rating_count.replace(",", ""))

                # 데이터 저장
                # JSON 파일에 저장
                save_to_json(CompanyInfo.from_data(company_name, rating_element.text, rating_count, converted_url))

                print(f"Rating: {rating_element.text}")
                print(f"Rating Count: {rating_count}")

                print(f"Link: {converted_url}")
                print(f"Company Name: {company_name}")
            except TimeoutException:
                print(f"Section {section_index}: Not found. Moving to next page if available.")
                break  # 섹션이 없으면 다음 페이지로 이동

        # 다음 페이지 버튼 확인 및 클릭
        try:
            # 이전 URL 저장
            _previous_url = driver.current_url

            # 다음 버튼 클릭 대기 및 클릭
            next_page_button = WebDriverWait(driver, 10).until(
                ec.element_to_be_clickable((By.XPATH, "//a[@class='btn_pgnext']"))
            )
            next_page_button.click()
            print("Next page button clicked. Waiting for the next page to load...")

            # URL 변경 확인
            WebDriverWait(driver, 10).until(
                ec.url_changes(_previous_url)
            )

            print("Page URL changed successfully.")
        except TimeoutException:
            print("Next page button not found or page did not load. Stopping.")
            break

finally:
    driver.quit()

    def p(self):
        return self.driver.find_element(By.XPATH, "//p[@class='txt']")
