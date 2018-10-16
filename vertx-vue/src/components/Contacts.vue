<template>
    <div class="contacts-holder">
        <h4 class="header-h4">CONTACTS</h4>
        <ul class="contacts">
            <li v-for="contact in contacts" v-on:click="open_chat(contact)">
                <img class="round-img" src="../assets/logo.png">
                <div class="contact-name">
                    <h4>{{contact.name}}</h4>
                    <p>{{contact.status}}</p>
                </div>
            </li>
        </ul>
    </div>
</template>
<style scoped>
    .header-h4 {
        line-height: 50px;
        border-bottom: 1px solid #dddddd;
        margin: 0;
    }

    .contacts-holder {
        width: 300px;
        border-right: 1px solid #eeeeee;
        height: auto;
    }

    .contact-name {
        flex-grow: 1;
        margin: 0;
        padding-left: 10px;
        line-height: 10px;
        text-align: start;
    }

    .contact-name > h4 {
        margin: 0;
        line-height: 20px;
    }

    .contact-name > p {
        margin: 0;
        line-height: 20px;
    }

    .contacts {
        height: 400px;
        max-height: 400px;
        overflow-y: auto;
        text-decoration: none;
        margin: 0;
        padding: 0;
        list-style: none;
    }

    .contacts > li {
        text-align: start;
        border-bottom: 1px solid #eee;
        padding: 10px 15px;
        margin: 0;
        display: flex;
        flex-direction: row;
        transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    }

    .contacts > li:hover {
        cursor: pointer;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 1px 1px rgba(0, 0, 0, 0.23);
    }
</style>
<script>
    import { EventBus } from '../events.js';

    export default {
        name: 'contacts',
        data() {
            return {
                contacts:[
                    {
                        name: 'Ayomide Fagbohungbe',
                        status: 'online',
                        id:'1ia9dfa'
                    }
                ]
            }
        },
        methods: {
            open_chat(contact) {
                this.$emit('open-chat', contact);
            }
        },
        mounted() {
            const self = this;
            //emit event when contacts finished loading

            EventBus.$on('app.connected', () => {
                const data = {
                    action: "get-conversations",
                    data: JSON.parse(localStorage.getItem('user'))
                };

                this.vertx_eb.send("api.data", data, {}, (err, message) => {
                    if (err) {
                        console.log('Something happened');
                        console.error(err);
                    } else {
                        self.contacts = message.body;
                        console.log(message.body);
                        console.log(self.contacts[0].name);
                        this.$emit('contacts-loaded', self.contacts);
                    }
                });
            });

        }
    }
</script>