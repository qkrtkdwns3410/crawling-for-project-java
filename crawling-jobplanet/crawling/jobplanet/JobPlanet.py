import logging
import os

from dotenv import load_dotenv
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager

import crawling.logging.logging_config as logging_config

# 로깅 설정
logging_config.setup_logging()
logger = logging.getLogger(__name__)

# .env 파일 로드
load_dotenv()

# 이메일 비밀번호 호출
EMAIL = os.getenv('EMAIL')
PASSWORD = os.getenv('PASSWORD')

logger.info(f'EMAIL, PASSWORD: {EMAIL}, {PASSWORD}')

# Service 객체 생성
driver_path = ChromeDriverManager().install()
chrome_driver_path = os.path.join(os.path.dirname(driver_path), "chromedriver.exe")

# WebDriver 초기화 시 service 매개변수 사용
driver = webdriver.Chrome(service=Service(executable_path=chrome_driver_path))

# 잡플래닛 로그인 페이지 이동
driver.get("https://www.jobplanet.co.kr/users/sign_in?_nav=gb")

# 이메일 입력 <input data-valid="false" id="user_email" name="user[email]" placeholder="이메일 주소" type="email" value="qkrtkdwns3410@naver.com">
email_input = driver.find_element(By.ID, "user_email")

# 비밀번호 입력
password_input = driver.find_element(By.ID, "user_password")

# 로그인 버튼 클릭 <button class="btn_sign_up" type="submit">이메일로 로그인</button>
login_button = driver.find_element(By.CLASS_NAME, "btn_sign_up")

# 로그인 버튼 클릭
email_input.send_keys(EMAIL)
password_input.send_keys(PASSWORD)
login_button.click()

# 레이팅 가져올 수 있는지 체크 ratings은..
"""

"""
ratings = []

try:
    for i in range(1, 11):
        url = f"https://www.jobplanet.co.kr/companies/{i}"
        driver.get(url)

        try:
            print(f"Current Page {i}: {driver.current_url}")
            # 특정 요소가 나타날 때까지 최대 10초 동안 기다립니다.
            WebDriverWait(driver, 5).until(
                ec.presence_of_element_located((By.TAG_NAME, "body"))
            )

            print(f"Page {i}: Page loaded successfully.")

            # 현재 URL이 메인 페이지로 리다이렉트되었는지 확인
            current_url = driver.current_url
            if "https://www.jobplanet.co.kr/companies" not in current_url:
                print(f"Page {i}: Redirected to main page, skipping.")
                continue

            # 현재페이지 로깅
            print(f"Current Page {i}: {current_url}")

            # 지정된 클래스명을 가진 요소를 기다림
            rating_element = WebDriverWait(driver, 5).until(
                ec.presence_of_element_located(
                    (By.XPATH, "//span[contains(@class, 'text-gray-800') and contains(@class, 'text-h5')]"))
            )

            ##평점 가져오기
            rating_value = rating_element.text
            ratings.append(rating_value)

            ### 회사이름 가져오기 currentUrl 에서 마지막/ 다음이 회사이름임
            company_name = current_url.split("/")[-1]

            print(f"Page {i}: Rating - {rating_value}")
        except Exception as e:
            logger.error(f"Page {i}: Error - {e}")

finally:
    driver.quit()

print("Collected Ratings:", ratings)
