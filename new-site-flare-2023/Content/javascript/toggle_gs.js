function toggleGS(divNum)
{

	
	switch (divNum) {
	case 'none':
	document.getElementById('1').style.display = 'none';
	document.getElementById('2').style.display = 'none';
	document.getElementById('3').style.display = 'none';
	document.getElementById('4').style.display = 'none';
	document.getElementById('5').style.display = 'none';
	document.getElementById('6').style.display = 'none';
		break;
		case '1':
	document.getElementById('1').style.display = 'block';
	document.getElementById('2').style.display = 'none';
	document.getElementById('3').style.display = 'none';
	document.getElementById('4').style.display = 'none';
	document.getElementById('5').style.display = 'none';
	document.getElementById('6').style.display = 'none';
	document.getElementById("scrollhere").scrollIntoView();
		break;
		case '2':
	document.getElementById('1').style.display = 'none';
	document.getElementById('2').style.display = 'block';
	document.getElementById('3').style.display = 'none';
	document.getElementById('4').style.display = 'none';
	document.getElementById('5').style.display = 'none';
	document.getElementById('6').style.display = 'none';
	document.getElementById("scrollhere").scrollIntoView();
		break;
		case '3':
	document.getElementById('1').style.display = 'none';
	document.getElementById('2').style.display = 'none';
	document.getElementById('3').style.display = 'block';
	document.getElementById('4').style.display = 'none';
	document.getElementById('5').style.display = 'none';
	document.getElementById('6').style.display = 'none';
	document.getElementById("scrollhere").scrollIntoView();
		break;
		case '4':
	document.getElementById('1').style.display = 'none';
	document.getElementById('2').style.display = 'none';
	document.getElementById('3').style.display = 'none';
	document.getElementById('4').style.display = 'block';
	document.getElementById('5').style.display = 'none';
	document.getElementById('6').style.display = 'none';
	document.getElementById("scrollhere").scrollIntoView();
		break;
		case '5':
	document.getElementById('1').style.display = 'none';
	document.getElementById('2').style.display = 'none';
	document.getElementById('3').style.display = 'none';
	document.getElementById('4').style.display = 'none';
	document.getElementById('5').style.display = 'block';
	document.getElementById('6').style.display = 'none';
	document.getElementById("scrollhere").scrollIntoView();
		break;
		case '6':
	document.getElementById('1').style.display = 'none';
	document.getElementById('2').style.display = 'none';
	document.getElementById('3').style.display = 'none';
	document.getElementById('4').style.display = 'none';
	document.getElementById('5').style.display = 'none';
	document.getElementById('6').style.display = 'block';
	document.getElementById("scrollhere").scrollIntoView();
		break;
		
	}
		

}