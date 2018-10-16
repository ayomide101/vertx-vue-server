// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VertxEventBus from 'vertx3-eventbus-client'
import { EventBus } from './events.js';

Vue.config.productionTip = false

Vue.mixin({
    data() {
        return {
            get vertx_eb() {
                const vert_eb = new VertxEventBus('http://127.0.0.1:8081/sockjs');

                vert_eb.onopen = () => {
                    console.log('Server connected');
                    EventBus.$emit('app.connected');
                };
                vert_eb.onerror = (err) => {
                    console.log('Error occured');
                    console.log(err);
                    EventBus.$emit('app.disconnected');
                };
                return vert_eb;
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
