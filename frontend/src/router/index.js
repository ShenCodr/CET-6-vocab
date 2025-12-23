import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import AddWord from "../views/AddWord.vue";
import Welcome from "../views/Welcome.vue";

const routes = [
  {
    path: "/",
    name: "Welcome",
    component: Welcome,
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
  },
  {
    path: "/add",
    name: "AddWord",
    component: AddWord,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
