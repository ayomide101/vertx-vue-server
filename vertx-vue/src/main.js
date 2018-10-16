// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import EventBus from 'vertx3-eventbus-client'

Vue.config.productionTip = false

Vue.mixin({
    data: function () {
        return {
            get eventbus() {
                // return new EventBus('127.0.0.1:8081/sockjs');
                return {
                    state : 1
                };
            },
            get api() {
                return '127.0.0.1:8081';
            },
            get state() {
                return {
                    isLoggedIn:false,
                    user : {

                    }
                }
            },
        }
    }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});
