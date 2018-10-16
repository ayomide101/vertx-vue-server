<template>
    <div class="login">
        <h1>LOGIN</h1>
        <div class="card">
            <img src="../assets/logo.png">
            <input placeholder="ENTER FIRST NAME" v-model="name"/>
            <p v-if="error.length > 0">{{error}}</p>
            <button v-on:click="login">ENTER</button>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'login',
        data() {
            return {
                name:'',
                error:''
            }
        },
        methods: {
            login: function(e) {
                e.preventDefault();
                this.error = "";
                if (this.name.length <= 0) {
                    this.error = "Name is too short"
                } else {
                    const self = this;
                    this.$http.post("http://"+this.api+"/login", { name:this.name })
                        .then(function(data) {
                            console.log('Success');
                            console.log(data);
                        }, function(error) {
                            self.error = "FAILED TO CONNECT";
                        });
                    localStorage.setItem("user", JSON.stringify({
                        name:this.name,
                    }));
                    this.$router.replace('conversations');
                }
            }
        }
    }
</script>
<style scoped>
    h1 {
        text-align: center;
        margin-bottom: 30px;
    }
    .card {
        height: 400px;
        width: 400px;
        margin: auto;
        border-radius: 4px;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
        border-left: 1px solid #eee;
        border-top: 1px solid #eee;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-content: center;
        align-items: center;
    }
    .card img {
        height: 150px;
        width: 150px;
        max-width: 100%;
        border-radius: 100%;
        margin-bottom: 30px;
        border:2px solid #eeeeee;
    }
    .card input {
        font-size: 16px;
        border: none;
        padding: 16px 48px;
        border-bottom: 3px solid #dddddd;
        outline: none;
        text-align: center;
        transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    }
    .card p {
        margin:0;
        text-align: start;
        color: orangered;
        font-weight: bold;
    }
    .card button {
        margin: 32px;
        border: none;
        background: dodgerblue;
        color: white;
        padding: 16px 32px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
        outline: none;
        transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    }
    .card button:hover,
    .card button:active,
    .card button:focus {
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
    }
    .card input:active,
    .card input:hover,
    .card input:focus {
        border-bottom: 3px solid dodgerblue;
    }
</style>