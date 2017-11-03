<?php
require_once "php/app.php";
openSession("./login.php");
include "php/ui.top.php"; ?>

<?php
$editorSave = "api/send-message.php"; // must execute save and update operations
$editorDelete = "api/delete-message.php?uid=$session[id]"; // must execute a delete operation
$editorLoad = "api/get-messages.php"; // must return array of objects
$listItemProperty = "subject";
$editorFormInputs = "php/ui.editor.messages.php";
$editorListTitle = "Messages";
$editorBtns = "
[
{
	label: 'Send',
	onclick: function() {
		console.log('sent!');
	}
},
{
	label: 'Reply',
	onclick: function() {
		console.log('reply!');
	}
}
]
";
include "./php/ui.editor.php";
?>

<?php include "php/ui.bottom.php"; ?>
