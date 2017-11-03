<?php
require_once "../php/app.php";
openSession();

$kid = array();
$kid['nid'] = $_POST['nid'];
$kid['name'] = $_POST['name'];
$kid['level'] = $_POST['level'];
$kid['address_title'] = $_POST['address_title'];
$kid['address_longitude'] = $_POST['address_longitude'];
$kid['address_latitude'] = $_POST['address_latitude'];
$kid['trans_id'] = $_POST['trans_id'];
$kid['parent1'] = $_POST['parent1'];
$kid['parent2'] = $_POST['parent2'];

echo saveKid($kid);
?>
