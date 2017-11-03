<?php
require_once "../php/app.php";
openSession();
$list = getKids();
echo json_encode($list);
?>
