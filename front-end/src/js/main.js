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
    changeMode();
   
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

function changeMode(){
    var element = document.body;
    element.dataset.bsTheme = 
        element.dataset.bsTheme == "light" ? "dark" : "light";
}

onAuthStateChanged(auth, user => {
    loaderElm.classList.add('d-none');
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

function createTask(task){
    const liElm = document.createElement('li');
    taskContainerElm.append(liElm);
    liElm.id = "task-" + task.id;
    liElm.className = 'd-flex justify-content-between p-1 px-5 align-items-center';

    liElm.innerHTML = `
        <div class="flex-grow-1 d-flex gap-2 align-items-center">
            <input class="form-check-input m-0" id="chk-task-${task.id}" type="checkbox" ${task.status ? "checked": ""}>
            <label class="flex-grow-1" for="chk-task-${task.id}">${task.description}</label>
        </div>
        <i class="delete bi bi-trash fs-4"></i>    
    `;
}

btnAddElm.addEventListener('click', ()=>{
    const taskDescription = txtElm.value;
    
    if (!taskDescription.trim()){
        txtElm.focus();
        txtElm.select();
        return;
    }

    fetch(`${API_URL}/tasks`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({description: txtElm.value, email: loggedUser.email})
    }).then(res => {
        if (res.ok){
            res.json().then(task => {
                createTask(task);
                txtElm.value = "";
                txtElm.focus();
            });
        }else{
            alert("Failed to add the task");
        }
    }).catch(err => {
        alert("Something went wrong, try again later");
    });
});


taskContainerElm.addEventListener('click', (e)=>{
    if (e.target?.classList.contains('delete')){

        const taskId = e.target.closest('li').id.substring(5);
        console.log("DELETE ", taskId);

        fetch(`${API_URL}/tasks/${taskId}`, {method: 'DELETE'})
        .then(res => {
            if (res.ok){
                e.target.closest("li").remove();
            }else{
                alert("Failed to delete the task");
            }
        }).catch(err => {
            alert("Something went wrong, try again later");
        });  

    } else if (e.target?.tagName === "INPUT"){
        // Todo: Update the task status in the back-end

        const taskId = e.target.closest('li').id.substring(5);
        const task = {
            description: e.target.nextElementSibling.innerText,
            status: e.target.checked,
            email: loggedUser.email
        };

        fetch(`${API_URL}/tasks/${taskId}`, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(task)
        }).then(res => {
            if (!res.ok){
                e.target.checked = false;
                alert("Failed to update the task status");
            }
        }).catch(err => {
            e.target.checked = false;
            alert("Something went wrong, try again");
        })
    }
});

