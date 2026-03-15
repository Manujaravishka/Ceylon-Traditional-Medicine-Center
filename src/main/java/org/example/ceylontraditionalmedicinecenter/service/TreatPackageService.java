package org.example.ceylontraditionalmedicinecenter.service;

import org.example.ceylontraditionalmedicinecenter.dto.TreatPackageDTO;

import java.util.List;

public interface TreatPackageService {
    int savePackage(TreatPackageDTO treatPackageDTO);

    List<TreatPackageDTO> getAllPackages();

    int updatePackage(Long id, TreatPackageDTO treatPackageDTO);

    int deletePackage(Long id);
}
