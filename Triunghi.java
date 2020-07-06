

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Triunghi extends Applet {
	Point[] cp; // punctele de control
	Point[] pctmediane;
	Point[] pctBisectoare;
	Point[] pctInaltimi;
	Image im;
	Graphics img;
	Button restart;
	Button bisectoare;
	Button mediana;
	Button inaltimi;
	int nrpuncte;
	int moveFlag;
	boolean deseneazaMediana;
	boolean deseneazaBisectoare;
	boolean deseneazaInaltimi;
	String[] litere = { "A", "B", "C", "G", "H", "I" };
	double razaCercCircumscris;
	Point centruCercCircum;
	int raza;
	boolean amTrasDeCerc;
	boolean amTrasDePunct;
	boolean mouseUp;

	public void init() {

		cp = new Point[3];
		pctmediane = new Point[3];
		pctBisectoare = new Point[3];
		pctInaltimi = new Point[3];
		im = createImage(3000, 3000);
		img = im.getGraphics();
		restart = new Button("start");
		bisectoare = new Button("bisectoare");
		mediana = new Button("mediana");
		inaltimi = new Button("inaltimi");
		add(restart);
		add(bisectoare);
		add(mediana);
		add(inaltimi);
		deseneazaMediana = false;
		deseneazaBisectoare = false;
		deseneazaInaltimi = false;
		amTrasDeCerc = false;
		amTrasDePunct = false;
		mouseUp = false;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		setBackground(Color.white);
		img.setColor(Color.black);
		img.clearRect(0, 0, size().width, size().height);

		// deseneaza punctele de control si poligonul de control

		if (nrpuncte < 3) {
			mediana.setEnabled(false);
			bisectoare.setEnabled(false);
			inaltimi.setEnabled(false);
		}
		if (nrpuncte == 3) {
			mediana.setEnabled(true);
			bisectoare.setEnabled(true);
			inaltimi.setEnabled(true);
			double laturaAB = distanta(cp[0], cp[1]);
			double laturaBC = distanta(cp[1], cp[2]);
			double laturaCA = distanta(cp[2], cp[0]);
			double unghiA = sin(laturaBC, laturaAB, laturaCA);
			double unghiB = sin(laturaCA, laturaAB, laturaBC);
			double unghiC = sin(laturaAB, laturaBC, laturaCA);
			centruCercCircum = cercCircumscris(unghiA, unghiB, unghiC);
			System.out.println("am tras de cerc = " + amTrasDeCerc);
			System.out.println("raza cercCircumscris " + razaCercCircumscris);
			if (amTrasDeCerc) {
				cp[0] = calculeazaNoiCoordPunct(cp[0], centruCercCircum);
				cp[1] = calculeazaNoiCoordPunct(cp[1], centruCercCircum);
				cp[2] = calculeazaNoiCoordPunct(cp[2], centruCercCircum);
				raza = (int) Math.round(razaCercCircumscris);
			} else {
				raza = (int) Math.round(getRaza(laturaAB, laturaBC, laturaCA));
				razaCercCircumscris = raza;
			}
			for (int i = 0; i < cp.length; i++) {
				System.out.println("x = " + cp[i].x + " " + " y = " + cp[i].y);
			}
			img.setColor(Color.blue);
			img.fillOval(centruCercCircum.x - 3, centruCercCircum.y - 3, 6, 6);
			img.setColor(Color.blue);
			img.drawOval(centruCercCircum.x - raza, centruCercCircum.y - raza, 2 * raza, 2 * raza);

		}
		if (nrpuncte == 3 && deseneazaMediana) {
			pctmediane[0] = mediana(cp[1], cp[2]);
			pctmediane[1] = mediana(cp[2], cp[0]);
			pctmediane[2] = mediana(cp[0], cp[1]);
			img.setColor(Color.yellow);
			img.fillOval(pctmediane[0].x - 3, pctmediane[0].y - 3, 6, 6);
			img.fillOval(pctmediane[1].x - 3, pctmediane[1].y - 3, 6, 6);
			img.fillOval(pctmediane[2].x - 3, pctmediane[2].y - 3, 6, 6);
			img.setColor(Color.DARK_GRAY);
			img.drawLine(cp[0].x, cp[0].y, pctmediane[0].x, pctmediane[0].y);
			img.drawLine(cp[1].x, cp[1].y, pctmediane[1].x, pctmediane[1].y);
			img.drawLine(cp[2].x, cp[2].y, pctmediane[2].x, pctmediane[2].y);
			img.setColor(Color.green);
			Point intersecteazaMediana = IntersectieMediana(cp[0], cp[1], cp[2]);
			img.setColor(Color.red);
			img.fillOval(intersecteazaMediana.x - 3, intersecteazaMediana.y - 3, 6, 6);
			img.setColor(Color.black);
			img.drawString(litere[3], intersecteazaMediana.x + 3, intersecteazaMediana.y + 3);
		}
		if (nrpuncte == 3 && deseneazaBisectoare) {
			pctBisectoare[0] = pctBisectoare(cp[1], cp[2], cp[0]);
			pctBisectoare[1] = pctBisectoare(cp[2], cp[0], cp[1]);
			pctBisectoare[2] = pctBisectoare(cp[1], cp[0], cp[2]);
			img.setColor(Color.yellow);
			img.fillOval(pctBisectoare[0].x - 3, pctBisectoare[0].y - 3, 6, 6);
			img.fillOval(pctBisectoare[1].x - 3, pctBisectoare[1].y - 3, 6, 6);
			img.fillOval(pctBisectoare[2].x - 3, pctBisectoare[2].y - 3, 6, 6);
			img.setColor(Color.DARK_GRAY);
			img.drawLine(cp[0].x, cp[0].y, pctBisectoare[0].x, pctBisectoare[0].y);
			img.drawLine(cp[1].x, cp[1].y, pctBisectoare[1].x, pctBisectoare[1].y);
			img.drawLine(cp[2].x, cp[2].y, pctBisectoare[2].x, pctBisectoare[2].y);
			img.setColor(Color.green);
			Point intersecteazaBisectoare = IntersectieBisect(cp[0], cp[1], cp[2]);
			img.setColor(Color.red);
			img.fillOval(intersecteazaBisectoare.x - 3, intersecteazaBisectoare.y - 3, 6, 6);
			img.setColor(Color.black);
			img.drawString(litere[5], intersecteazaBisectoare.x + 3, intersecteazaBisectoare.y + 3);
		}
		if (nrpuncte == 3 && deseneazaInaltimi) {
			pctInaltimi[0] = pctInaltimi(cp[0], cp[1], cp[2]);
			pctInaltimi[1] = pctInaltimi(cp[1], cp[2], cp[0]);
			pctInaltimi[2] = pctInaltimi(cp[2], cp[0], cp[1]);
			img.setColor(Color.yellow);
			img.fillOval(pctInaltimi[0].x - 3, pctInaltimi[0].y - 3, 6, 6);
			img.fillOval(pctInaltimi[1].x - 3, pctInaltimi[1].y - 3, 6, 6);
			img.fillOval(pctInaltimi[2].x - 3, pctInaltimi[2].y - 3, 6, 6);
			img.setColor(Color.DARK_GRAY);
			img.drawLine(cp[0].x, cp[0].y, pctInaltimi[0].x, pctInaltimi[0].y);
			img.drawLine(cp[1].x, cp[1].y, pctInaltimi[1].x, pctInaltimi[1].y);
			img.drawLine(cp[2].x, cp[2].y, pctInaltimi[2].x, pctInaltimi[2].y);
			Point intersecteazaInaltimi = intersectieInaltimi(cp[0], cp[1], cp[2]);
			img.setColor(Color.red);
			img.fillOval(intersecteazaInaltimi.x - 3, intersecteazaInaltimi.y - 3, 6, 6);
			img.setColor(Color.black);
			img.drawString(litere[4], intersecteazaInaltimi.x + 3, intersecteazaInaltimi.y + 3);
		}

		if (cp.length > 0) {
			if (nrpuncte > 1) {
				for (int i = 0; i < nrpuncte; i++) {
					img.setColor(Color.black);
					if (i + 1 == nrpuncte) {
						img.drawLine(cp[i].x, cp[i].y, cp[0].x, cp[0].y);
					} else {
						img.drawLine(cp[i].x, cp[i].y, cp[i + 1].x, cp[i + 1].y);
					}
				}
			}
			for (int i = 0; i < nrpuncte; i++) {
				img.setColor(Color.yellow);
				img.fillOval(cp[i].x - 3, cp[i].y - 3, 6, 6);
				img.setColor(Color.black);
				img.drawString(litere[i], cp[i].x + 3, cp[i].y + 3);
			}

		}

		g.drawImage(im, 0, 0, this);
	}

	public boolean action(Event e, Object o) {
		if (((Button) e.target).getLabel() == "start") {
			((Button) e.target).setLabel("restart");
			repaint();
			return true;
		}
		if (((Button) e.target).getLabel() == "restart") {
			cp = new Point[3];
			nrpuncte = 0;
			deseneazaMediana = false;
			deseneazaBisectoare = false;
			deseneazaInaltimi = false;
			amTrasDeCerc = false;
			amTrasDePunct = false;
			repaint();
			((Button) e.target).setLabel("start");
			return true;
		}
		if (((Button) e.target).getLabel() == "mediana" && nrpuncte == 3) {
			if (deseneazaMediana) {
				deseneazaMediana = false;
				repaint();
				return true;
			} else {
				deseneazaMediana = true;
				pctmediane = new Point[3];
				deseneazaBisectoare = false;
				deseneazaInaltimi = false;
				repaint();
				return true;
			}
		}
		if (((Button) e.target).getLabel() == "bisectoare" && nrpuncte == 3) {
			if (deseneazaBisectoare) {
				deseneazaBisectoare = false;

				repaint();
				return true;
			} else {
				deseneazaBisectoare = true;
				pctBisectoare = new Point[3];
				deseneazaMediana = false;
				deseneazaInaltimi = false;
				repaint();
				return true;
			}
		}

		if (((Button) e.target).getLabel() == "inaltimi" && nrpuncte == 3) {
			if (deseneazaInaltimi) {
				deseneazaInaltimi = false;

				repaint();
				return true;
			} else {
				deseneazaInaltimi = true;
				pctInaltimi = new Point[3];
				deseneazaMediana = false;
				deseneazaBisectoare = false;
				repaint();
				return true;
			}
		}

		return false;
	}

	public boolean mouseDown(Event evt, int x, int y) {
		Point point = new Point(x, y);

		if (nrpuncte != 3) {
			cp[nrpuncte] = point;
			nrpuncte++;
		} else {
			for (int i = 0; i < cp.length; i++) {
				for (int j = -8; j < 9; j++)
					for (int k = -8; k < 9; k++)
						if (point.equals(new Point(cp[i].x + j, cp[i].y + k))) {
							moveFlag = i;
							amTrasDePunct = true;
						}
			}
			if (!amTrasDePunct) {
				for (int j = -8; j < 9; j++) {
					for (int k = -8; k < 9; k++) {

						if (Math.round(razaCercCircumscris) == Math
								.round(distanta(centruCercCircum, new Point(x + j, y + k)))) {
							amTrasDeCerc = true;
						}

					}
				}
			}
		}
		repaint();
		return true;
	}

	public boolean mouseDrag(Event eve, int x, int y) {

		if (amTrasDeCerc) {
			razaCercCircumscris = (int) Math.round(distanta(centruCercCircum, new Point(x, y)));

		}
		if (amTrasDePunct) {
			cp[moveFlag].move(x, y);
		}
		System.out.println("am intrat in mouse drag");
		repaint();
		return true;
	}

	public boolean mouseUp(Event eve, int x, int y) {

		amTrasDePunct = false;
		if (amTrasDeCerc) {
			amTrasDeCerc = false;
			mouseUp = true;
		}

		System.out.println("am intrat in mouse up");
		return true;
	}

	/**************************************************************/

	public double distanta(Point p1, Point p2) {
		double d = 0;
		d = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		return d;
	}

	public Point cercCircumscris(double unghiA, double unghiB, double unghiC) {
		Point p = new Point();

		// TODO:LOGICA DE CREARE A PCT
		double x = (cp[0].x * unghiA + cp[1].x * unghiB + cp[2].x * unghiC) / (unghiA + unghiB + unghiC);
		double y = (cp[0].y * unghiA + cp[1].y * unghiB + cp[2].y * unghiC) / (unghiA + unghiB + unghiC);
		p.setLocation(x, y);
		return p;
	}

	public double sin(double latOpus, double latStanga, double latDreapta) {
		double result = (Math.pow(latStanga, 2) + Math.pow(latDreapta, 2) - Math.pow(latOpus, 2))
				/ (2 * latStanga * latDreapta);
		result = Math.sin(2 * Math.acos(result));
		return result;
	}

	public double getRaza(double latOpus, double latStanga, double latDreapta) {
		double result = (latOpus * latStanga * latDreapta)
				/ (Math.sqrt((latOpus + latDreapta + latStanga) * (latOpus + latDreapta - latStanga)
						* (latOpus - latDreapta + latStanga) * (latDreapta + latStanga - latOpus)));
		return result;
	}

	public Point mediana(Point varfStanga, Point varfDreapta) {
		Point p = new Point();
		p.x = (varfStanga.x + varfDreapta.x) / 2;
		p.y = (varfStanga.y + varfDreapta.y) / 2;
		return p;
	}

	public double getPanta(Point varfStanga, Point varfDreapta) {
		double m = (double) (varfStanga.y - varfDreapta.y) / (double) (varfStanga.x - varfDreapta.x);
		return m;
	}

	public double getDistantaPanaLaPicBisectoarei(double latStanga, double latDreapta, double latOpusUnghi) {
		double x = 0;
		x = (latOpusUnghi * latStanga) / (latDreapta + latStanga);
		return x;
	}

	public Point pctBisectoare(Point varfStanga, Point varfDreapta, Point varf) {
		Point pctBisectoare = new Point();
		double panta = getPanta(varfStanga, varfDreapta);
		double latOpusUnghi = distanta(varfStanga, varfDreapta);
		double latStanga = 0;
		double latDreapta = 0;
		Point pctPlecare = new Point();
		if (varfStanga.x <= varfDreapta.x) {
			latStanga = distanta(varf, varfStanga);
			latDreapta = distanta(varf, varfDreapta);
			pctPlecare = varfStanga;
		} else {
			latStanga = distanta(varf, varfDreapta);
			latDreapta = distanta(varf, varfStanga);
			pctPlecare = varfDreapta;
		}
		double distanta = getDistantaPanaLaPicBisectoarei(latStanga, latDreapta, latOpusUnghi);
		pctBisectoare.x = (int) Math.round(pctPlecare.x + distanta * Math.sqrt(1 / (1 + Math.pow(panta, 2))));
		pctBisectoare.y = (int) Math.round(pctPlecare.y + panta * distanta * Math.sqrt(1 / (1 + Math.pow(panta, 2))));
		return pctBisectoare;
	}

	public Point pctInaltimi(Point varf, Point varfStanga, Point varfDreapta) {
		Point pctInaltime = new Point();
		if (varfStanga.x <= varfDreapta.x) {

		} else {
			Point pointHolder = new Point();
			pointHolder = varfStanga;
			varfStanga = varfDreapta;
			varfDreapta = pointHolder;
		}
		double pantaInaltime = (-1.0) / getPanta(varfStanga, varfDreapta);
		double pantaBaza = getPanta(varfStanga, varfDreapta);
		pctInaltime.x = (int) Math.round((varfStanga.y - varf.y + pantaInaltime * varf.x - varfStanga.x * pantaBaza)
				/ (pantaInaltime - pantaBaza));
		pctInaltime.y = (int) Math.round(varf.y - pantaInaltime * (varf.x - pctInaltime.x));

		return pctInaltime;
	}

	public Point IntersectieMediana(Point varf, Point varfStanga, Point varfDreapta) {
		Point p = new Point();
		p.x = (varf.x + varfStanga.x + varfDreapta.x) / 3;
		p.y = (varf.y + varfStanga.y + varfDreapta.y) / 3;
		return p;
	}

	public Point IntersectieBisect(Point varf, Point varfStanga, Point varfDreapta) {
		Point p = new Point();
		p.x = (int) Math.round(((distanta(varfStanga, varfDreapta) * varf.x + distanta(varf, varfDreapta) * varfStanga.x
				+ distanta(varf, varfStanga) * varfDreapta.x)
				/ (distanta(varfStanga, varfDreapta) + distanta(varf, varfDreapta) + distanta(varf, varfStanga))));
		p.y = (int) Math.round(((distanta(varfStanga, varfDreapta) * varf.y + distanta(varf, varfDreapta) * varfStanga.y
				+ distanta(varf, varfStanga) * varfDreapta.y)
				/ (distanta(varfStanga, varfDreapta) + distanta(varf, varfDreapta) + distanta(varf, varfStanga))));
		return p;
	}

	public Point intersectieInaltimi(Point varf, Point varfStanga, Point varfDreapta) {
		Point p = new Point();
		double pantaAB = getPanta(varf, varfStanga);
		double pantaCF = (-1.0) / pantaAB;
		double pantaBC = getPanta(varfStanga, varfDreapta);
		double pantaAD = (-1.0) / pantaBC;
		p.y = (int) Math.round(((pantaCF * varf.y + pantaCF * pantaAD * varfDreapta.x - pantaCF * pantaAD * varf.x
				- pantaAD * varfDreapta.y) / (pantaCF - pantaAD)));
		p.x = (int) Math.round(((pantaCF * varfDreapta.x - varfDreapta.y + p.y) / (pantaCF)));
		return p;
	}

	public Point calculeazaNoiCoordPunct(Point punct, Point centruCerc) {
		Point p = new Point();
		double panta = getPanta(punct, centruCerc);
		if (punct.y >= centruCerc.y) {
			p.y = (int) Math.round(razaCercCircumscris / (Math.sqrt(1 / (panta * panta) + 1)) + centruCerc.y);
			p.x = (int) Math.round((p.y - centruCerc.y) / panta + centruCerc.x);
		} else {
			panta = getPanta(centruCerc, punct);
			p.y = (int) Math.round(razaCercCircumscris / (Math.sqrt(1 / (panta * panta) + 1)) + centruCerc.y);
			p.x = (int) Math.round((p.y - centruCerc.y) / -panta + centruCerc.x);

		}

		System.out.println("raza : " + razaCercCircumscris + " " + " panta = " + panta);
		return p;
	}

}
