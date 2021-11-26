# ✔️ Git Convention

# **Branch**

master

|

develop

|

feature (각 기능 단위)

- feature의 기능 단위는 Jira Story 기준
    - 필요에 따라 통합해서 사용해도 괜찮음
- 예시
    - feat/fe/mypage
    
    

# **Commit**

```
type: [BE] 행위 요약

body

#Jira 이슈번호
```

```
feat: [BE] 대표 부캐 설정 api 구현

행위에 대한 자세한 설명 (What & Why)

#S05P21A303-99
```

- 본문은 설명 필요할 때 선택적 작성

### 참고

- [https://udacity.github.io/git-styleguide/](https://udacity.github.io/git-styleguide/)
- [https://medium.com/@steveamaza/how-to-write-a-proper-git-commit-message-e028865e5791](https://medium.com/@steveamaza/how-to-write-a-proper-git-commit-message-e028865e5791)

### type 종류
| type     | 설명                             | 비고                       |
| -------- | -------------------------------- | -------------------------- |
| feat     | 새로운 기능 추가                 |                            |
| fix      | 버그 수정                        |                            |
| docs     | 문서 수정                        |                            |
| style    | 코드 포맷팅, 세미콜론 누락 등    | 코드 변경 없음             |
| refactor | 코드 리팩토링                    |                            |
| test     | 테스트 관련 코드 수정            | 프로덕션 코드 변경 없음    |
| chore    | 빌드 작업, 패키지 관리자 구성 등 | 프로덕션 코드 변경 없음    |
| merge    | 머지할 때                        | ex) merge: [BE] Login 기능 |
| design   | css, 디자인 수정                 |                            |


# **Git 사용법**

- 문제가 발생할 경우 팀에 바로 공유하기
- `git branch`, `git status`, `git pull` 의 습관화 ✅
- merge conflict를 최소화 합시다

---

1. develop branch로 이동 후 최신 버전 업데이트
   
    ```bash
    $ git switch develop
    $ git pull origin develop
    ```
    
2. 각 기능 단위 feature branch 생성
   
    ```bash
    $ git switch -c <브랜치명>
    ```
    
3. 해당 branch에 변경 내용 add - commit - push
   
    ```bash
    $ git add <파일 또는 폴더>
    $ git commit -m '커밋 메시지'
    $ git push origin <브랜치명>
    ```
    
4. GitLab에서 `merge request` 생성
5. 팀원에게 승인 받고 `merge` (코드 리뷰도 받기를 권장☝️)
6. 로컬에서 merge 완료된 feature branch 삭제
   
    ```bash
    $ git switch develop
    $ git merge <브랜치명>
    $ git pull # develop보다 커밋한 횟수만큼 앞서 있으므로
    $ git branch -d <브랜치명>
    ```
