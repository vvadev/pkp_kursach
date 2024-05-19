package com.example.demo.service;

import com.example.demo.entity.ClubMembership;
import com.example.demo.repository.ClubMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubMembershipService {
    private final ClubMembershipRepository clubMembershipRepository;

    @Autowired
    public ClubMembershipService(ClubMembershipRepository clubMembershipRepository) {
        this.clubMembershipRepository = clubMembershipRepository;
    }

    public void saveMembership(ClubMembership membership) {
        clubMembershipRepository.save(membership);
    }

    public List<ClubMembership> getMemberships() {
        return clubMembershipRepository.findAll();
    }

    public ClubMembership getMembershipById(Long id) {
        return clubMembershipRepository.findById(id).orElse(null);
    }

    public ClubMembership updateMembership(Long id, ClubMembership membership) {
        ClubMembership existingMembership = clubMembershipRepository.findById(id).orElse(null);
        if (existingMembership != null) {
            existingMembership.setStudent(membership.getStudent());
            existingMembership.setClub(membership.getClub());
            existingMembership.setJoinDate(membership.getJoinDate());
            return clubMembershipRepository.save(existingMembership);
        }
        return null;
    }

    public String deleteMembership(Long id) {
        clubMembershipRepository.deleteById(id);
        return "Club membership with ID " + id + " has been deleted.";
    }
}
