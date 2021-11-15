import * as firebase from 'firebase';

const config = {
    apiKey: "AIzaSyB4ZmoHupffIt49JZkQIO1kWjnXNdutmGU",
    authDomain: "comu-14e68.firebaseapp.com",
    databaseURL: "https://comu-14e68-default-rtdb.firebaseio.com",
    projectId: "comu-14e68",
    storageBucket: "comu-14e68.appspot.com",
};

firebase.initializeApp(config);
export default firebase;