// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import EventBus from 'vertx3-eventbus-client'

Vue.config.productionTip = false

Vue.mixin({
    data() {
        return {
            get eventbus() {
                return new EventBus('http://127.0.0.1:8081/sockjs');
            },
            get api() {
                return '127.0.0.1:8081';
            }
        }
    }
});

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    components: {App},
    template: '<App/>'
});
