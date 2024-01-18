// Import the functions you need from the SDKs you need

import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getAnalytics } from "firebase/analytics";

// TODO: Add SDKs for Firebase products that you want to use

// https://firebase.google.com/docs/web/setup#available-libraries


// Your web app's Firebase configuration

// For Firebase JS SDK v7.20.0 and later, measurementId is optional

const firebaseConfig = {

  apiKey: "AIzaSyC_ELT4CDCmqCvP0uK1uq4PjcDxOyE_sNA",

  authDomain: "to-do-app-demo1.firebaseapp.com",

  projectId: "to-do-app-demo1",

  storageBucket: "to-do-app-demo1.appspot.com",

  messagingSenderId: "458503961373",

  appId: "1:458503961373:web:55b268ad1a70c96f327ff4",

  measurementId: "G-PW3ZNDFPYR"

};


// Initialize Firebase

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const analytics = getAnalytics(app);

export {app, auth};