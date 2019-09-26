var waitingForMsg = false;
/*function getMessage(){
    if(!waitingMsg){
        waitingMsg = true;
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200)
            {
                var elem = document.getElementById('message');
                // elem.innerHTML = xhttp.responseText + elem.innerHTML;
                elem.innerHTML = xhttp.responseText;
                waitingMsg = false;
            }
        };
        xhttp.open('get', '/Chat'/!*?t=' + new Date()*!/, true);
        xhttp.send();
    }
}*/
var counter = 0;
function getMessage(){//if not waiting for a message, create a request and wait until the response is ready and then set the 'message' div's content to what you receive in the responseText
    if(!waitingForMsg){
        waitingForMsg = true;
        var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function checkState(){
                document.getElementById("states").innerHTML = document.getElementById("states").innerHTML+xhttp.readyState+" ";
                if(this.readyState == 4 && this.status == 200)
                {
                    var elem = document.getElementById('message');
                    elem.innerHTML = xhttp.responseText;
                    waitingForMsg = false;

                    if (counter>3) {
                        counter=0;
                        document.body.style.backgroundImage = "url('birdsoldier.jpg')";
                        setTimeout(function () {
                            document.body.style.backgroundImage = "url('catbacon.jpg')";
                        }, 500);
                    }else{counter++;}

                }
            };
        xhttp.open('get', '/Chat', true);
        xhttp.send();
    }
    /*if (counter == 10){
        counter=0;
        document.body.style.backgroundImage = "url('birdsoldier.jpg')";   }else{
        document.body.style.backgroundImage = "url('catbacon.jpg')";
    }
    counter++;*/
}
setInterval(getMessage, 100);//make getMessage() happen every 10 secs



function postMessage(){//happens everytime the user clicks on the button or presses enter while in the textfield
    var xhttp = new XMLHttpRequest();
        xhttp.open('post','/Chat', true);
    xhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    //var name = encodeURI(document.getElementById('name').value);
    var msg = encodeURI(document.getElementById('msg').value);
    document.getElementById('msg').value='';
    // xhttp.send('msg='+msg+'&t='+new Date());
    xhttp.send('msg='+msg);
}