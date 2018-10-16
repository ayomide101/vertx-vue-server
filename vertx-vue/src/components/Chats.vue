<template>
    <div class="conversation-holder">
        <div class="error-holder" v-if="messages.length == 0">
            <img src="../assets/logo.png"/>
            <h3>NO CONVERSATIONS</h3>
        </div>
        <div class="header" v-if="messages.length > 0">
            <img class="round-img" src="../assets/logo.png">
            <div class="contact-name">
                <h4>Ayomide Fagbohungbe</h4>
                <p>Message</p>
            </div>
        </div>
        <ul class="chats" v-if="messages.length > 0">
            <li class="chat" v-for="message in 20" :class="{recieve : message % 3 == 1 }">
                <p>Plenty message Plenty message lenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty messagelenty message Plenty message</p>
            </li>
        </ul>
        <div class="chat-input" v-if="messages.length > 0">
            <textarea placeholder="Enter message" v-model="message"></textarea>
            <button v-on:click="send_message(message)" v-on:keyup="typing">SEND</button>
        </div>
    </div>
</template>

<style>
    .conversation-holder {
        width: 500px;
        height: auto;
        display: flex;
        flex-direction: column;
    }

    .chats {
        height: 100%;
        max-height: 500px;
        overflow-y: auto;
        margin: 0;
        padding: 0;
        text-decoration: none;
        list-style: none;
        position: relative;
        width: 100%;
        max-width: 100%;
        display: flex;
        flex-direction: column;
        box-sizing: border-box;
        background: #f5f5f5;
    }

    .chats .chat {
        height: auto;
        width: 100%;
        text-decoration: none;
        box-sizing: border-box;
    }

    .chats .chat p {
        background: #ffffff;
        float: right;
        display:flex;
        font-size: 14px;
        line-height: 20px;
        max-width: 70%;
        padding: 6px 10px;
        text-align: left;
        overflow-wrap: break-word;
        border-radius: 4px;
        margin: 8px 16px;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
    }

    .chats .chat.recieve p {
        float: left;
    }

    .header {
        border-bottom: 1px solid #dddddd;
        margin: 0;
        display: flex;
        flex-direction: row;
        padding: 0 16px;
        justify-content: center;
        align-content: center;
        align-items: center;
        height: 50px;
    }

    .contact-name {
        flex-grow: 1;
        margin: 0;
        padding-left: 10px;
        line-height: 10px;
        text-align: start;
        align-items: start;
        align-content: center;
        justify-content: center;
        display: flex;
        flex-direction: column;
    }

    .header > .contact-name p {
        font-size: 12px;
        line-height: normal;
        margin: 0;

    }

    .header > .contact-name h4 {
        line-height: 10px;
        margin: 0;
    }

    .header > .round-img {
        width: 35px;
        height: 35px;
    }

    .chat-input {
        height: 50px;
        border-top: 1px solid #eee;
        display: flex;
    }

    .chat-input > textarea {
        flex-grow: 1;
        border: none;
        padding: 8px 16px;
        text-align: start;
        outline: none;
        resize: none;
        font-size: 14px;
    }

    .chat-input > button {
        border: none;
        font-weight: bold;
        color: dodgerblue;
        padding: 0 15px;
        border-left: 1px solid #eee;
        border-bottom-right-radius: 6px;
    }

    .chat-input > button:hover {
        background: dodgerblue;
        color: white;
        cursor: pointer;
    }

    .error-holder {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-content: center;
        align-items: center;
        height: 100%;
        max-height: 100%;
        overflow: hidden;
    }

    .error-holder > img {
        max-width: 100%;
        width: 40%;
        height: auto;
        filter: grayscale(100%);
        opacity: 0.5;
    }

    .error-holder > h3 {
        text-align: center;
        color: #ddd;
    }
</style>

<script>
    export default {
        name: 'chats',
        props: {
            user: {
                type: Object,
                default: () => {
                    return {
                        name: 'Ayomide'
                    };
                }
            }
        },
        data() {
            return {
                messages: [
                    {
                        name: 1
                    }
                ],
                message: ''
            }
        },
        methods: {
            send_message: (message) => {
                console.log('ouput -> ' + message);
            },
            typing: (e) => {
                console.log('Typing');
                console.log(e);
            }
        }
    }
</script>
