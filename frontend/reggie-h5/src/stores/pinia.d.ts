import "pinia-plugin-persistedstate";
// https://github.com/piniajs/pinia-plugin-persistedstate#typescript
declare module "pinia" {
  export interface DefineStoreOptionsBase<S> {
    persist?: boolean | { storage?: Storage; key?: string; paths?: string[] };
  }
}
