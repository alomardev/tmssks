<?php
require_once "php/app.php";
openSession("./login.php");

include "php/ui.top.php"; ?>

<?php
$editorSave = "api/save-trans.php"; // must execute save and update operations
$editorDelete = "api/delete-trans.php"; // must execute a delete operation
$editorLoad = "api/get-trans.php"; // must return array of objects
$listItemProperty = "num_plate";
$editorFormInputs = "php/ui.editor.trans.php";
$editorListTitle = "Select Transportation";
include "./php/ui.editor.php";
?>

<?php include "php/ui.bottom.php"; ?>
