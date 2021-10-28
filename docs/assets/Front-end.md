# 🐶 Front-end

## 파일명

- vue 파일명 : pascal case
  
    ex) `PascalCase.vue`
    
- js 파일명 : camel case
  
    ex) `camelCase.js`



## 함수명

- camel case



## 변수명

- camel case



## 컴포넌트

- import한 컴포넌트 : kebab case
  
    ex) `<kebab-case />`
    
- 컴포넌트 이름은 합성어 사용
  
    ex) ~~Todo.vue~~ → `TodoItem.vue`
    
    

## 코드 스타일

📄 [Style Guide - Vue.js](https://kr.vuejs.org/v2/style-guide)

- **우선순위 A는 꼭 지키기**, 우선순위 B는 왠만하면 지키기
- 우선순위 C, D는 필요에 따라 참고하기



### **[엘리먼트 속성 순서](https://kr.vuejs.org/v2/style-guide/index.html#%EC%97%98%EB%A6%AC%EB%A8%BC%ED%8A%B8-%EC%86%8D%EC%84%B1-%EC%88%9C%EC%84%9C-%EC%B6%94%EC%B2%9C%ED%95%A8)**

**엘리먼트 및 컴포넌트의 속성은 언제나 일정한 순서로 정렬되어야 합니다.**

아래는 권장하는 엘리먼트 속성 순서입니다. 유형별로 나누어 놓았으므로, 커스텀 속성이나 directive 역시 이에 맞추어 정렬하면 됩니다.

1. **정의(Definition)** (컴포넌트 옵션을 제공하는 속성)
    - `is`
2. **리스트 렌더링(List Rendering)** (같은 엘리먼트의 변형을 여러 개 생성하는 속성)
    - `v-for`
3. **조건부(Conditionals)** (엘리먼트가 렌더링되는지 혹은 보여지는지 여부를 결정하는 속성)
    - `v-if`
    - `v-else-if`
    - `v-else`
    - `v-show`
    - `v-cloak`
4. **렌더 변경자(Render Modifiers)** (엘리먼트의 렌더링 방식을 변경하는 속성)
    - `v-pre`
    - `v-once`
5. **전역 인지(Global Awareness)** (컴포넌트 바깥의 지식을 요구하는 속성)
    - `id`
6. **유일한 속성(Unique Attributes)** (유일한 값을 가질 것을 요구하는 속성)
    - `ref`
    - `key`
    - `slot`
7. **양방향 바인딩(Two-Way Binding)** (바인딩과 이벤트를 결합하는 속성)
- `v-model`
1. **기타 속성** (따로 언급하지 않은 속성들)
2. **이벤트(Events)** (컴포넌트 이벤트 리스너를 지정하는 속성)
    - `v-on`
3. **내용(Content)** (엘리먼트의 내용을 덮어쓰는 속성)
    - `v-html`
    - `v-text`
    
    

### Vue Style Sample

```jsx
<template>
  <div>
    <!-- 여기부터 코드 작성 -->
  </div>
</template>

<script>
export default {
  name: "Template",
  // componets
  components: {},
  // props
  props: {},
  // data
  data() {
    return {};
  },
  // computed
  computed: {},
  // lifecycle hook
  created() {},
  mounted() {},
  updated() {},
  destroyed() {},
  // methods
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./Template.scss";
</style>
```



### Vuex Style Sample

📄 [Vuex가 무엇인가요? | Vuex](https://vuex.vuejs.org/kr/)

```jsx
import SERVER from "@/api/api";
import axios from "axios";

const todoStore = {
  namespaced: true,
  state: {
    todoList: [
      { id: 1, text: '...', done: true },
      { id: 2, text: '...', done: false }
    ]
  },
  getters: {
    doneTodoList: state => {
      return state.todoList.filter(todo => todo.done)
    }
  },
  mutations: {
    SET_TODO_LIST(state, data) {
      state.todoList = data;
    },
  },
  actions: {
    // To Do List 가져오기
    async getTodoList({ getters, commit }, data) {
      await axios
        .get(
          SERVER.URL + SERVER.ROUTES.getTodoList
        )
        .then((res) => {
          // code
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};

export default todoStore;
```

