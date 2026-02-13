package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.ModelView;
import itu.framework.backoffice.dtos.CreateVehicule;
import itu.framework.backoffice.entities.Vehicule;

import java.util.List;

@Controller("/api/vehicules")
public class VehiculeController {

    @GetMapping
    public ModelView getAllVehicule() throws Exception {
        List<Vehicule> vehicules = Vehicule.findAll(Vehicule.class);
        ModelView view = new ModelView("vehicule/vehicule-list");
        view.addObject("vehicules", vehicules);
        return view;
    }

    @GetMapping("/{id}")
    public ModelView getVehiculeById(int id) throws Exception {
        Vehicule vehicule = (Vehicule) Vehicule.findById(Vehicule.class, id);
        ModelView view = new ModelView("vehicule/vehicule-fiche");
        view.addObject("vehicule", vehicule);
        return view;
    }

    @GetMapping("/form")
    public ModelView showVehiculeForm(@RequestParam("id") Integer id) throws Exception {
        ModelView view = new ModelView("vehicule/vehicule-form");

        if (id != null) {
            Vehicule vehicule = (Vehicule) Vehicule.findById(Vehicule.class, id);
            view.addObject("vehicule", vehicule);
        }

        return view;
    }

    @PostMapping
    public ModelView createVehicule(CreateVehicule dto) throws Exception {
        try {
            Vehicule vehicule = new Vehicule();

            // Si un ID est fourni, c'est une mise à jour
            if (dto.getRef() != null && !dto.getRef().isEmpty()) {
                vehicule.setRef(dto.getRef());
            }
            vehicule.setNbrPlace(dto.getNbrPlace());
            vehicule.setTypeCarburant(dto.getTypeCarburant());

            vehicule = (Vehicule) vehicule.save();

            return ModelView.redirect("/api/vehicules/" + vehicule.getId());
        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&&link=/api/vehicules/form");
        }
    }

    @PostMapping("/{id}")
    public ModelView updateVehicule(int id, CreateVehicule dto) throws Exception {
        try {
            Vehicule vehicule = (Vehicule) Vehicule.findById(Vehicule.class, id);

            if (vehicule != null) {
                vehicule.setRef(dto.getRef());
                vehicule.setNbrPlace(dto.getNbrPlace());
                vehicule.setTypeCarburant(dto.getTypeCarburant());
                vehicule.save();
            }

            return ModelView.redirect("/api/vehicules/" + id);
        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&&link=/api/vehicules/" + id);
        }
    }
}
