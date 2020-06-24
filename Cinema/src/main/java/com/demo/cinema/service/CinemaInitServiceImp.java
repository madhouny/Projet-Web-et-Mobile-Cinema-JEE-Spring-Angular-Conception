package com.demo.cinema.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;












import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;












import com.demo.cinema.dao.CategorieRepository;
import com.demo.cinema.dao.CinemaRepository;
import com.demo.cinema.dao.FilmRepository;
import com.demo.cinema.dao.PlaceRepository;
import com.demo.cinema.dao.ProjectionRepository;
import com.demo.cinema.dao.SalleRepository;
import com.demo.cinema.dao.SceanceRepository;
import com.demo.cinema.dao.TicketRepository;
import com.demo.cinema.dao.VilleRepository;
import com.demo.cinema.entities.Categorie;
import com.demo.cinema.entities.Cinema;
import com.demo.cinema.entities.Film;
import com.demo.cinema.entities.Place;
import com.demo.cinema.entities.Projection;
import com.demo.cinema.entities.Salle;
import com.demo.cinema.entities.Sceance;
import com.demo.cinema.entities.Ticket;
import com.demo.cinema.entities.Ville;

@Service
@Transactional
public class CinemaInitServiceImp  implements ICinemaInitService{
	@Autowired
	private VilleRepository villeRepository;
	
	@Autowired
	private CinemaRepository cinemaRepository;
	
	@Autowired
	private SalleRepository salleRepository;
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@Autowired
	private SceanceRepository sceanceRepository;
	
	@Autowired
	private FilmRepository filmRepository;
	
	@Autowired
	private ProjectionRepository projectionRepository;
	
	@Autowired
	private CategorieRepository categorieRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Override
	public void initVille() {
		List<String> listVilles = new ArrayList<>();
		listVilles.add("Paris");
		listVilles.add("Rabat");
		listVilles.add("Montpellier");
		
		listVilles.forEach(v->{
			Ville ville = new Ville();
			ville.setName(v);
			villeRepository.save(ville);
			
			
		});
		
		
	}

	@Override
	public void initCinema() {
		villeRepository.findAll().forEach(v->{
			Stream.of("Megarama","IMax","Founoun","Chahrazad","Daouliz")
			.forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+ (int)(Math.random() *7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});;
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i = 0 ; i <cinema.getNombreSalles();i++){
				Salle salle = new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
				
			}
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i = 0 ; i < salle.getNombrePlace(); i++){
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSceances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00")
		.forEach(s->{
			Sceance sceance = new Sceance();
			try {
				sceance.setHeureDebut(dateFormat.parse(s));
				sceanceRepository.save(sceance);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire","Action","Fiction","Drama").forEach(c->{
			Categorie categorie= new Categorie();
			categorie.setName(c);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	public void initFilms() {
		double [] durees= new double[]{1.5,2,1,2.5,3};
		List<Categorie> categorie = categorieRepository.findAll();
		Stream.of("Casa del Papel","Game Of Trones","IronMan","Seigneur Des Anneaux","Titanic")
		.forEach(f->{
			Film film = new Film();
			film.setTitre(f);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(f.replaceAll(" ", ""));
			film.setCategorie(categorie.get(new Random().nextInt(categorie.size())));
			filmRepository.save(film);
			
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices = new double[]{30,50,60,70,90,100};
		villeRepository.findAll().forEach(v->{
			v.getCinemas().forEach(c->{
				c.getSalles().forEach(s->{
					filmRepository.findAll().forEach(film->{
						sceanceRepository.findAll().forEach(sceance->{
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(s);
							projection.setSeance(sceance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		});
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}

}
