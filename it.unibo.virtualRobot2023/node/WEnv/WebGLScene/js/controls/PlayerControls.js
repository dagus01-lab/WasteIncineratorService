/*
PlayerControls
*/
import * as THREE from '../../node_modules/three/build/three.module.js'
import eventBus       from '../eventBus/EventBus.js'
import eventBusEvents from '../eventBus/events.js'

export default (mesh, camera, config, collisionManager, mirror) => {
	
    const keycodes = {
        W: 87,
        A: 65,
        S: 83,
        D: 68,
        R: 82,
        F: 70,
        Z: 90
    }
	
	let lastTime = 0;
	
    let forward = false
    let backward = false
    let rotating = false
    var tween ;

    setCameraPositionRelativeToMesh(camera, mesh)

	function stopRotating(){ //April2022
	    rotating = false
	    if( tween != undefined){
	        console.log("PlayerControls | stopRotatinggg tween=" + tween )
	        tween.stop();
	    }
	}

    function onKeyDown(keyCode, duration=300, moveindex ) {
        console.log("PlayerControls | onKeyDown  moveindex=" + moveindex + " keyCode=" + keyCode + " duration="+duration)
        if(keyCode === keycodes.W) forward = true
        else if(keyCode === keycodes.S) backward = true
        else if(keyCode === keycodes.Z){ stopRotating() } //April2022}
        else if(keyCode === keycodes.D) rotate(-Math.PI/2, duration, moveindex)
        else if(keyCode === keycodes.A) rotate(Math.PI/2, duration, moveindex)
    }

    function onKeyUp(keyCode) {
        if(keyCode === keycodes.W) forward = false
        else if(keyCode === keycodes.S) backward = false;
    }

    function rotate(angle, duration, moveindex) {
        console.log("PlayerControls rotateee rotating="+rotating + " duration=" + duration)
        //duration -= 50  //GEN2023 ???
        if(rotating){  //April2020: la rotazione precedente ancora in corso inibisce la nuova
            console.log("PlayerControls | rotate  REJECTED"  )
            return
        }
        const finalAngle = mesh.rotation.y + angle
        rotating = true
        tween = new TWEEN.Tween(mesh.rotation)  //https://sbcode.net/threejs/tween/
            .to({ y: finalAngle }, duration  )
            .easing(TWEEN.Easing.Quadratic.InOut)
            .onComplete( () => {  //Alla fine della rotazione emette l'evento (vedi EventtBus 19)
                rotating = false;
                console.log("PlayerControls | rotate  complete duration=" + duration + " moveindex=" + moveindex)
                eventBus.post(eventBusEvents.endmove, moveindex)  //EventBus 27
             })
            .start()
    }    

    function update(time) {
		const delta = time - lastTime;
		
        const matrix = new THREE.Matrix4()
        matrix.extractRotation( mesh.matrix )

        const directionVector = new THREE.Vector3( 0, 0, -1 )
        directionVector.applyMatrix4(matrix)
    
	if(forward || backward) {
            const direction = backward ? 1 : -1
            const stepVector = directionVector.multiplyScalar( 60 * config.speed * direction * ( delta ))
            const tPosition  = mesh.position.clone().add(stepVector)
//console.log("PlayerControls | update timeeee =" + time)
            //if( !mirror ){
            	const collision = collisionManager.checkCollision(tPosition, mirror)		
            	if( !collision ) {
               		 mesh.position.add(stepVector)
                	 camera.position.add(stepVector)
             	}  
	   // }//mirror          
        } else 
            collisionManager.checkCollision(mesh.position)
		
		lastTime = time;
    }
    
    function resetPosition() {
        mesh.position.x = config.position.x
        mesh.position.z = config.position.y

        setCameraPositionRelativeToMesh(camera, mesh)
    }

    function setCameraPositionRelativeToMesh(camera, mesh) {
        camera.position.x = mesh.position.x
        camera.position.z = mesh.position.z + 20

        camera.lookAt(new THREE.Vector3(mesh.position.x, 0, mesh.position.z))
    }
	
	return {
        resetPosition,
		onKeyDown,
		onKeyUp,
		update, stopRotating
	}
}

/*
SEQUENZA quando premo il tasto D
main.js:34 onKeyDown from main event.keyCode=68 moveIndex=undefined
PlayerControls.js:34 PlayerControls | onKeyDown  moveindex=undefined keyCode=68 duration=300
PlayerControls.js:48 rotateee rotating=false duration=300
PlayerControls.js:61 PlayerControls | rotate  complete duration=300
SocketIO.js:34 SocketIO endmove: undefined
*/
