package com.demo.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.demo.cinema.entities.Cinema;
import com.demo.cinema.entities.Ville;
@RepositoryRestResource
public interface VilleRepository extends JpaRepository<Ville, Long> {

}
