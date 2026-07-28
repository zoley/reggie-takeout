import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useUserStore = defineStore(
  "user",
  () => {
    const token = ref<string>("");
    const userInfo = ref<any>(null);

    const userName = computed(() => userInfo.value?.name || "");

    function setLogin(data: { id: string; name: string; [key: string]: any }) {
      token.value = data.id;
      userInfo.value = data;
    }

    function logout() {
      token.value = "";
      userInfo.value = null;
    }

    return { token, userInfo, userName, setLogin, logout };
  },
  {
    persist: {
      storage: sessionStorage,
    },
  },
);
