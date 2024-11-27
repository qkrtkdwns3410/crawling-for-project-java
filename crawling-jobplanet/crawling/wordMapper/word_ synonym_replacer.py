from rapidfuzz import process, fuzz

from crawling.file_util import file_util


def filter_candidates(name, candidates, threshold=50):
    return [candidate for candidate in candidates if fuzz.ratio(name, candidate) >= threshold]

jobplanet_file_location = '../jobplanet/company_data.json'
jobplanet_json = file_util.read_json_file(jobplanet_file_location)

list1 = ["삼성전자", "LG화학", "SK하이닉스", "네이버"]
list2 = ["삼성", "엘지화학", "SK Hynix", "Naver Corporation", "SK", "Google Korea"]

results = []
for name1 in list1:
    # 사전 필터링 (길이 차이 제한)
    filtered_list2 = [name for name in list2 if abs(len(name1) - len(name)) <= 5]

    # 유사도 계산
    match = process.extractOne(name1, filtered_list2, scorer=fuzz.ratio)
    if match and match[1] > 70:  # 유사도 70 이상만
        results.append((name1, match[0], match[1]))

for result in results:
    print(result)
