function toogle() {
		check = document.getElementById('expand');
		console.log(check);
		console.log(check.checked == true);
		if (check.checked == true) {
			console.log('expandiendo');
			expand();
			check.checked = true;
		} else {
			console.log('comprimiendo');
			collapse();
			check.checked = false;
		}
	}
	function collapse() {
		menu = document.getElementById("menu");
		menu.style.display = 'none';
		main = document.getElementById('main');
		main.classList.remove('span10');
		main.classList.add('span11');
		wrap = document.getElementById('menuWrapper');
		wrap.classList.remove('span2');
		wrap.classList.add('span1');
	}
	function expand () {
		menu = document.getElementById("menu");
		menu.style.display = '';
		main = document.getElementById('main');
		main.classList.remove('span11');
		main.classList.add('span10');
		wrap = document.getElementById('menuWrapper');
		wrap.classList.remove('span1');
		wrap.classList.add('span2');
	}