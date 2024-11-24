## 크롤링 프로젝트

### requirements 필수 라이브러리 설치법
```
pip install -r requirements.txt
```


### .env 파일 로컬 커밋 대상에서 지우는 방법
```
git update-index --assume-unchanged <env경로>
```

### .env 파일 로컬 커밋 대상에 추가하는 방법
```
git update-index --no-assume-unchanged <env경로>
```

### 포함되지 않도록 한 파일 목록을 보는 방법
```
git ls-files -v | grep '^h'
```
