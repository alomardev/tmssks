<?php
require_once "php/app.php";
openSession("./login.php");
include "php/ui.top.php"; ?>

<?php
$editorSave = "api/save-kid.php"; // must execute save and update operations
$editorDelete = "api/delete-kid.php"; // must execute a delete operation
$editorLoad = "api/get-kids.php"; // must return array of objects
$listItemProperty = "name";
$editorFormInputs = "php/ui.editor.kids.php";
$editorListTitle = "Select Kid";
include "./php/ui.editor.php";
?>

<?php include "php/ui.bottom.php"; ?>
