		</div>
	</div>
</section>
</div>
<footer>
	<div class="wrapper">
	<div class="clear-float">
		<img class="float-right" src="res/drawable/org_logo.png">
		<p>The website TMSSKS is part of the outcomes of the graduation project named "Transport Monitoring System for School Kids Safety."
		Sponsored by College of Computer Sciences and Information Technology, King Faisal University.<br><br>

		Done by: Abdulrahman M. Alomar, Ammar Alabdulqader, Khalid Alshehri<br>
		Supervised by: Dr. Shakeel Ahemd<br>
		Comittee: Dr. Sayd Afaq Husain, Dr. Billal Ahmed<br>
		<br>
		(Spring 2017)
		</p>
	</div>
	</div>
</footer>

<?php
if (isset($script)) {
	if (is_array($script)) {
		foreach ($script as $src) {
			echo "<script src='$src'></script>";
		}
	} else {
		echo "<script src='$script'></script>";
	}
}
?>
</body>
</html>
