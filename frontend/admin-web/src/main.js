import { createApp } from "vue";
import "element-plus/dist/index.css";
import "./common/css/style.css";
import App from "./App.vue";
import router from "./router";

const app = createApp(App);
app.use(router);
app.mount("#app");
