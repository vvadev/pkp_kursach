package com.example.demo.service;

import com.example.demo.entity.Club;
import com.example.demo.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public void saveClub(Club club) {
        clubRepository.save(club);
    }

    public List<Club> getClubs() {
        return clubRepository.findAll();
    }

    public Club getClubById(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    public Club updateClub(Long id, Club club) {
        Club existingClub = clubRepository.findById(id).orElse(null);
        if (existingClub != null) {
            existingClub.setClubName(club.getClubName());
            existingClub.setAdvisor(club.getAdvisor());
            return clubRepository.save(existingClub);
        }
        return null;
    }

    public String deleteClub(Long id) {
        clubRepository.deleteById(id);
        return "Club with ID " + id + " has been deleted.";
    }
}
