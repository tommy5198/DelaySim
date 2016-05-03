function rnd() {
	return ((Math.random() + Math.random() + Math.random() + Math.random() + Math.random() + Math.random()) - 3) / 3;
}

var user = document.getElementById('user');
var mean = 1000;
var std = 200;
var stepLength = 5;
var MaxW = 400;
var left = 40;
user.style.left = left;
document.addEventListener('keydown', function(e){
	delay = mean + std * rnd();
	setTimeout(function(){
		if(e.keyCode == 37) 
			left -= stepLength;
		if(e.keyCode == 39)
			left += stepLength;
		user.style.left = left;	
	}, delay);
});

var info = document.getElementById('info');
info.innerHTML = "mean = " + mean + ", std = " + std;