
function alertTooLongPost(){
    var postcontent = document.getElementById("postcontent").value
    if (postcontent.length >= 255) {
        alert("You have " + postcontent.length + " marks. Make it less than 255!")
    }
}

function alertTooLongPostComment(){
    var postcontent = document.getElementById("postcommentcontent").value
    if (postcontent.length >= 255) {
        alert("You have " + postcontent.length + " marks. Make it less than 255!")
    }
}

function alertTooLongPhotoComment(){
    var postcontent = document.getElementById("photocommentcontent").value
    if (postcontent.length >= 255) {
        alert("You have " + postcontent.length + " marks. Make it less than 255!")
    }
}