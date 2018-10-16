import Vue from 'vue'
import Router from 'vue-router'
import Resource from 'vue-resource'
import Conversations from '@/components/Conversations'
import Login from '@/components/Login'

Vue.use(Router);
Vue.use(Resource);

export default new Router({
    routes: [
        {
            path: '/',
            name: 'login',
            component: Login
        },
        {
            path: '/conversations',
            name: 'conversations',
            component: Conversations
        }
    ]
})
