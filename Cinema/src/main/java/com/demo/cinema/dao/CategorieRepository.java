package com.demo.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.demo.cinema.entities.Categorie;
import com.demo.cinema.entities.Cinema;
@RepositoryRestResource
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

}
