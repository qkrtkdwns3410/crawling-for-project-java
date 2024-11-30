import logging

from rapidfuzz import fuzz, process

from crawling.file_util import file_util

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def filter_candidates(name, candidates, threshold=50):
    return [candidate for candidate in candidates if fuzz.ratio(name, candidate) >= threshold]

jobplanet_file_location = '../jobplanet/company_data.json'
jobkorea_file_location = '../jobkorea/jobkorea_jobs.json'

jobplanet_json = file_util.read_json_file(jobplanet_file_location)
jobkorea_json = file_util.read_json_file(jobkorea_file_location)

# jobplanet_json에서 company_name 컬럼을 추출하여 리스트로 만듦
# jobkorea_json에서 company 컬럼을 추출하여 리스트로 만듦
jobplanet_companies = [entry['company_name'] for entry in jobplanet_json]
jobkorea_companies = [entry['company_name'] for entry in jobkorea_json]

logging.info(f"jobplanet_companies: {jobplanet_companies}")
logging.info(f"jobkorea_companies: {jobkorea_companies}")

results = []
for jobplaent_company_name in jobplanet_companies:
    print(f"Processing {jobplaent_company_name}...")

    # 사전 필터링 (길이 차이 제한)
    filtered_list2 = [jobkorea_company_name for jobkorea_company_name in jobkorea_companies if abs(len(jobplaent_company_name) - len(jobkorea_company_name)) <= 5]

    # 유사도 계산
    match = process.extractOne(jobplaent_company_name, filtered_list2, scorer=fuzz.ratio)
    if match and match[1] > 70:  # 유사도 70 이상만
        results.append((jobplaent_company_name, match[0], match[1]))

for result in results:
    print(result)
