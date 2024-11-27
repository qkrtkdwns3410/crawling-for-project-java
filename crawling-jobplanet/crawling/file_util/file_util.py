import json
import os


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

def read_json_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        return json.load(f)
