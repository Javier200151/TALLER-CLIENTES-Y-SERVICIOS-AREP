var imgs = ['yoda.jpg','among.jpg','halo.jpg'];
var rotacion = 0;

function actualizar(){
    var image = document.getElementById('imagen');
    image.src = imgs[rotacion];
}

function cambiarImagen(){
    rotacion += 1;
    if(rotacion>=3){
		rotacion=0;
	}
    actualizar();
}