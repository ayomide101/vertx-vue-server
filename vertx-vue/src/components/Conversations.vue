<template>
    <div>
        <h1>CONVERSATIONS</h1>
        <p v-if="eventbus.state == 0" class="connect-status">connecting... <button v-on:click="logout" class="logout">logout</button></p>
        <p v-if="eventbus.state == 1" class="connect-status">connected <button v-on:click="logout" class="logout">logout</button></p>
        <p v-if="eventbus.state == 2" class="connect-status">disconnecting... <button v-on:click="logout" class="logout">logout</button></p>
        <p v-if="eventbus.state == 3" class="connect-status">disconnected <button v-on:click="logout" class="logout">logout</button></p>
        <div class="main">
            <contacts v-on:contacts-loaded="onContactsLoaded" v-on:open-chat="onOpenChat"></contacts>
            <chats :user="active_contact"></chats>
        </div>
    </div>
</template>

<style>
    ul {
        text-decoration: none;
        margin: 0;
        padding: 0;
    }

    .logout {
        border: none;
        font-weight: bold;
        color: dodgerblue;
        padding: 0 32px;
        cursor: pointer;
        margin: 0 15px;
    }
    .main {
        border-radius: 6px;
        display: flex;
        flex-direction: row;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
        border-left: 1px solid #eee;
        border-top: 1px solid #eee;
        width: 800px;
        background: #ffffff;
        margin:30px auto;
    }

    .round-img {
        width: 40px;
        max-width: 100%;
        height: 40px;
        border-radius: 100%;
        border: 1px solid #eeeeee;
    }


    h1 {
        text-align: center;
        margin:30px 10px auto;
    }

    .connect-status {
        margin-left: 34px;
        margin-top: 0;
        text-align: center;
    }
</style>

<script>
    import Contacts from "./Contacts";
    import Chats from "./Chats";


    export default {
        components: {
            Chats,
            Contacts
        },
        name: 'conversations',
        data() {
            return {
                conversations: [],
                active_contact:{
                    name: "Fola Shade"
                }
            }
        },
        methods: {
            onContactsLoaded: function (contacts)  {
                //Assign the first loaded contact as the active contact
                this.active_contact = contacts[0]; //First contact
            },
            onOpenChat: function (contact) {
                //Assign the selected contact as the active contact
                //So that the conversations can be loaded
                this.active_contact = contact;
            },
            logout: function (e) {
                e.preventDefault();
                localStorage.removeItem('user');
                this.$router.replace('/');
            }
        }
    }
</script>