var imgs = ['img/yoda.jpg','img/among.jpg','img/halo.jpg'];
var rotacion = 0;

function actualizar(){
    $("#imagen").attr("src",imgs[rotacion]);
}

function cambiarImagen(){
    rotacion += 1;
    if(rotacion>=3){
		rotacion=0;
	}
    actualizar();
}

$(document).ready(function(){
	actualizar();
	$("#cambiarImagen").click(function(){
		cambiarImagen();
	});
});
