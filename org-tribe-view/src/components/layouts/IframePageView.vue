<template>

  <iframe :id="id" :src="url" frameborder="0" width="100%" height="800px" scrolling="auto"></iframe>

</template>

<script>
  import PageLayout from '../page/PageLayout'
  import RouteView from './RouteView'
  import Vue from 'vue'

  export default {
    name: "IframePageContent",
    data() {
      return {
        url: "",
        id: ""
      }
    },
    created() {
      this.goUrl()
    },
    updated() {
      this.goUrl()
    },
    watch: {
      $route(to, from) {
        this.goUrl();
      }
    },
    methods: {
      goUrl() {
        console.log(this.$route.meta.url);
        let url = this.$route.meta.url + '?X-Access-Token=' + Vue.ls.get('Access-Token');
        let id = this.$route.path;
        this.id = id;
        console.log(url)
        if (url !== null && url !== undefined) {
          this.url = url;
          //window.open(this.url);
        }
      }
    }
  }
</script>

<style>
</style>