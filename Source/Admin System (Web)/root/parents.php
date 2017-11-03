<?php
require_once "php/app.php";
openSession("./login.php");

include "php/ui.top.php"; ?>

<?php
$editorSave = "api/save-user.php"; // must execute save and update operations
$editorDelete = "api/delete-user.php"; // must execute a delete operation
$editorLoad = "api/get-users.php?role=2"; // must return array of objects
$listItemProperty = "name";
$editorFormInputs = "php/ui.editor.users.php";
$editorListTitle = "Select Parents";
include "./php/ui.editor.php";
?>

<?php include "php/ui.bottom.php"; ?>
