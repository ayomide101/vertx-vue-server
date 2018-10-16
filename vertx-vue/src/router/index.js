import Vue from 'vue'
import Router from 'vue-router'
import Resource from 'vue-resource'
import Conversations from '@/components/Conversations'
import Login from '@/components/Login'

Vue.use(Router);
Vue.use(Resource);

const router = new Router({
    routes: [

        {
            path: '/',
            redirect: {
                name: "login"
            }
        },
        {
            path: "/login",
            name: "login",
            component: Login
        },
        {
            path: '/conversations',
            name: 'conversations',
            component: Conversations
        }
    ]
});

router.beforeEach((to, from, next) => {
    // redirect to login page if not logged in and trying to access a restricted page
    const publicPages = ['/login'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = localStorage.getItem('user');

    if (authRequired && !loggedIn) {
        return next('/login');
    }

    next();
});

export default router;