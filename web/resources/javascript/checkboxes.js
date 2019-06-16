/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal 
btn.onclick = function() {
  modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}


var checked = false;
function toggleAll2() {
    var checkboxes = document.getElementsByClassName('check[]');
    if (checked) {
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = false;
        }
    } else {
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = true;
        }
    }
    checked = !checked;
    toggle();
}

function toggle() {
    var checkboxes = document.getElementsByClassName('check[]');
    var checked = 0;
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked === true) {
            checked++;
        }
        if (checked > 1) {
            break;
        }
    }
    if (checked === 1) {
        document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
        document.getElementById("exampleModalLabel").innerHTML = "Apagar a atividade?";
        document.getElementById("modalid").innerHTML = "Irá apagar as cópias desta atividade e as suas associações a papéis,produtos,processos e padrões.";
    } else if (checked > 1) {
        document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
        document.getElementById("exampleModalLabel").innerHTML = "Apagar as atividades?";
        document.getElementById("modalid").innerHTML = "Irá apagar as cópias destas atividades e as suas associações a papéis,produtos,processos e padrões.";
    } else {
        document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
    }
}

function toggleAll() {
    var checkboxes = document.getElementsByClassName('check[]');
    if (checked) {
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = false;
        }
    } else {
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = true;
        }
    }
    checked = !checked;
}

function enableAssociate() {
    var checkboxes = document.getElementsByClassName('check[]');
    var check = false;
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked === true) {
            check = true;
            break;
        }
    }
    if (check === true)
        document.getElementsByClassName("btn btn-primary")[0].disabled = false;
    else
        document.getElementsByClassName("btn btn-primary")[0].disabled = true;
}

function toggleproduto() {
    var checkboxes = document.getElementsByClassName('check[]');
    var checked = 0;
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked === true) {
            checked++;
        }
        if (checked > 1) {
            break;
        }
    }
    if (checked === 1) {
        document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
      //  document.getElementById("exampleModalLabel").innerHTML = "Apagar o Produto?";
     //   document.getElementById("modalid").innerHTML = message;
    } else if (checked > 1) {
        document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
     //   document.getElementById("exampleModalLabel").innerHTML = "Apagar os produtos?";
    //    document.getElementById("modalid").innerHTML = "Irá apagar as associções ás atividades.";
    } else {
        document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
    }
}

//grupo 6
function toggleAllForPadrao(){
    var checkboxes = document.getElementsByClassName('check[]');
    if(checked){
        for(var i = 0; i < checkboxes.length; i++){
            checkboxes[i].checked = false;
        }
    }
    else{
        for(var i = 0; i < checkboxes.length; i++){
            checkboxes[i].checked = true;
        }
    }
    checked = !checked;
    toggleForPadrao();
}

function toggleForPadrao(){
        var checkboxes = document.getElementsByClassName('check[]');
        var checked = 0;
        for(var i = 0; i < checkboxes.length; i++){
            if(checkboxes[i].checked === true){
                checked++;
            }
            if(checked > 1){
               break;
            }
        }
	if(checked === 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar o padrao?";
            document.getElementById("modalid").innerHTML = "Irá apagar as cópias deste padrao e as suas associações a atividades e agrupamentos.";
	}
        else if(checked > 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar os padrões?";
            document.getElementById("modalid").innerHTML = "Irá apagar as cópias destes padrões e as suas associações a atividades e agrupamentos.";
        }
	else{
           document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
	}
}

function toggleAgrupamentos(){
        var checkboxes = document.getElementsByClassName('check[]');
        var checked = 0;
        for(var i = 0; i < checkboxes.length; i++){
            if(checkboxes[i].checked === true){
                checked++;
            }
            if(checked > 1){
               break;
            }
        }
	if(checked === 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar o Agrupamento?";
            document.getElementById("modalid").innerHTML = "Irá apagar a associção às frases e agrupamentos.";
	}
        else if(checked > 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar os Agrupamentos?";
            document.getElementById("modalid").innerHTML = "Irá apagar a associção às frases e agrupamentos.";
        }
	else{
           document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
	}
}

function togglefrase(){
        var checkboxes = document.getElementsByClassName('check[]');
        var checked = 0;
        for(var i = 0; i < checkboxes.length; i++){
            if(checkboxes[i].checked === true){
                checked++;
            }
            if(checked > 1){
               break;
            }
        }
	if(checked === 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar a Frase?";
            document.getElementById("modalid").innerHTML = "Irá apagar a associção aos agrupamentos.";
	}
        else if(checked > 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar as frases?";
            document.getElementById("modalid").innerHTML = "Irá apagar as associções aos agrupamentos.";
        }
	else{
           document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
	}
}