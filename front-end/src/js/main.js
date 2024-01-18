import { signInWithPopup, signOut, onAuthStateChanged, GoogleAuthProvider } from 'firebase/auth';
import {auth} from './firebase.js';

const txtElm = document.querySelector('#txt');
const btnAddElm = document.querySelector('#btn-add');
const taskContainerElm = document.querySelector('#task-container');
const {API_URL} = process.env;
const googleProvider = new GoogleAuthProvider();
const btnSignInElm = document.querySelector("#btn-sign-in");
const btnSingOutElm = document.querySelector("#btn-sign-out");
const loginOverlayElm = document.querySelector("#login-overlay");
let loggedUser = null;
const loaderElm = document.querySelector("#loader");


const modeElm = document.querySelector('#mode');
modeElm.addEventListener('click', (e) => {
    myFunction();
   
    if(e.target?.classList.contains('bi-brightness-high-fill')){
        e.target.classList.remove('bi-brightness-high-fill');
        e.target.classList.add('bi-moon-stars-fill');
        e.target.classList.remove('fs-3');
        e.target.classList.add('fs-4');
    }else if(e.target?.classList.contains('bi-moon-stars-fill')){
        e.target.classList.remove('bi-moon-stars-fill');
        e.target.classList.add('bi-brightness-high-fill');
        e.target.classList.remove('fs-4');
        e.target.classList.add('fs-3');
    }
});

function myFunction(){
    var element = document.body;
    element.dataset.bsTheme = 
        element.dataset.bsTheme == "light" ? "dark" : "light";
}

onAuthStateChanged(auth, user => {
    loggedUser = user;
    console.log(loggedUser);
    if (user){
        loginOverlayElm.classList.add('d-none');
        loadAllTasks();
    }else{
        loginOverlayElm.classList.remove('d-none');
    }
});

btnSignInElm.addEventListener('click', ()=>{
    signInWithPopup(auth, googleProvider);
});

btnSingOutElm.addEventListener('click', ()=>{
    signOut(auth);   
});


function loadAllTasks(){
    fetch(`${API_URL}/tasks?email=${loggedUser.email}`).then(res => {
        if(res.ok){
            res.json().then(taskList => {
                taskContainerElm.querySelectorAll("li").forEach(li => li.remove());
                taskList.forEach(task => createTask(task));
            });
        }else{
            alert("Failed to load task List");
        }
    }).catch(err =>{
        alert("Something went wrong, try again later")
    });
}