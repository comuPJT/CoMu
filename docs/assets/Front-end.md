# πΆ Front-end

## νμΌλͺ

- vue νμΌλͺ : pascal case
  
    ex) `PascalCase.vue`
    
- js νμΌλͺ : camel case
  
    ex) `camelCase.js`



## ν¨μλͺ

- camel case



## λ³μλͺ

- camel case



## μ»΄ν¬λνΈ

- importν μ»΄ν¬λνΈ : kebab case
  
    ex) `<kebab-case />`
    
- μ»΄ν¬λνΈ μ΄λ¦μ ν©μ±μ΄ μ¬μ©
  
    ex) ~~Todo.vue~~ β `TodoItem.vue`
    
    

## μ½λ μ€νμΌ

π [Style Guide - Vue.js](https://kr.vuejs.org/v2/style-guide)

- **μ°μ μμ Aλ κΌ­ μ§ν€κΈ°**, μ°μ μμ Bλ μ λ§νλ©΄ μ§ν€κΈ°
- μ°μ μμ C, Dλ νμμ λ°λΌ μ°Έκ³ νκΈ°



### **[μλ¦¬λ¨ΌνΈ μμ± μμ](https://kr.vuejs.org/v2/style-guide/index.html#%EC%97%98%EB%A6%AC%EB%A8%BC%ED%8A%B8-%EC%86%8D%EC%84%B1-%EC%88%9C%EC%84%9C-%EC%B6%94%EC%B2%9C%ED%95%A8)**

**μλ¦¬λ¨ΌνΈ λ° μ»΄ν¬λνΈμ μμ±μ μΈμ λ μΌμ ν μμλ‘ μ λ ¬λμ΄μΌ ν©λλ€.**

μλλ κΆμ₯νλ μλ¦¬λ¨ΌνΈ μμ± μμμλλ€. μ νλ³λ‘ λλμ΄ λμμΌλ―λ‘, μ»€μ€ν μμ±μ΄λ directive μ­μ μ΄μ λ§μΆμ΄ μ λ ¬νλ©΄ λ©λλ€.

1. **μ μ(Definition)**Β (μ»΄ν¬λνΈ μ΅μμ μ κ³΅νλ μμ±)
    - `is`
2. **λ¦¬μ€νΈ λ λλ§(List Rendering)**Β (κ°μ μλ¦¬λ¨ΌνΈμ λ³νμ μ¬λ¬ κ° μμ±νλ μμ±)
    - `v-for`
3. **μ‘°κ±΄λΆ(Conditionals)**Β (μλ¦¬λ¨ΌνΈκ° λ λλ§λλμ§ νΉμ λ³΄μ¬μ§λμ§ μ¬λΆλ₯Ό κ²°μ νλ μμ±)
    - `v-if`
    - `v-else-if`
    - `v-else`
    - `v-show`
    - `v-cloak`
4. **λ λ λ³κ²½μ(Render Modifiers)**Β (μλ¦¬λ¨ΌνΈμ λ λλ§ λ°©μμ λ³κ²½νλ μμ±)
    - `v-pre`
    - `v-once`
5. **μ μ­ μΈμ§(Global Awareness)**Β (μ»΄ν¬λνΈ λ°κΉ₯μ μ§μμ μκ΅¬νλ μμ±)
    - `id`
6. **μ μΌν μμ±(Unique Attributes)**Β (μ μΌν κ°μ κ°μ§ κ²μ μκ΅¬νλ μμ±)
    - `ref`
    - `key`
    - `slot`
7. **μλ°©ν₯ λ°μΈλ©(Two-Way Binding)**Β (λ°μΈλ©κ³Ό μ΄λ²€νΈλ₯Ό κ²°ν©νλ μμ±)
- `v-model`
1. **κΈ°ν μμ±**Β (λ°λ‘ μΈκΈνμ§ μμ μμ±λ€)
2. **μ΄λ²€νΈ(Events)**Β (μ»΄ν¬λνΈ μ΄λ²€νΈ λ¦¬μ€λλ₯Ό μ§μ νλ μμ±)
    - `v-on`
3. **λ΄μ©(Content)**Β (μλ¦¬λ¨ΌνΈμ λ΄μ©μ λ?μ΄μ°λ μμ±)
    - `v-html`
    - `v-text`
    
    

### Vue Style Sample

```jsx
<template>
  <div>
    <!-- μ¬κΈ°λΆν° μ½λ μμ± -->
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

π [Vuexκ° λ¬΄μμΈκ°μ? | Vuex](https://vuex.vuejs.org/kr/)

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
    // To Do List κ°μ Έμ€κΈ°
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

