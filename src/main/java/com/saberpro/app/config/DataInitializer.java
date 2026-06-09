package com.saberpro.app.config;

import com.saberpro.app.model.*;
import com.saberpro.app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository     usuarioRepository,
            DocenteRepository     docenteRepository,
            CoordinadorRepository coordinadorRepository,
            FacultadRepository    facultadRepository,
            EstudianteRepository  estudianteRepository,
            ResultadoRepository   resultadoRepository) {

        return args -> {

            // ── FACULTADES ───────────────────────────────────────────────
            Facultad facTec = facultadRepository.findByNombre("Tecnología")
                .orElseGet(() -> {
                    Facultad f = new Facultad();
                    f.setNombre("Tecnología");
                    f.setDescripcion("Facultad de Tecnología");
                    return facultadRepository.save(f);
                });

            Facultad facIng = facultadRepository.findByNombre("Ingeniería")
                .orElseGet(() -> {
                    Facultad f = new Facultad();
                    f.setNombre("Ingeniería");
                    f.setDescripcion("Facultad de Ingeniería");
                    return facultadRepository.save(f);
                });

            // ── ADMINISTRADOR ────────────────────────────────────────────
            if (!usuarioRepository.existsByCorreo("admin@saberpro.edu.co")) {
                Usuario admin = new Usuario();
                admin.setNumeroCedula("1000000001");
                admin.setPrimerNombre("Admin");
                admin.setPrimerApellido("Sistema");
                admin.setCorreo("admin@saberpro.edu.co");
                admin.setContrasena("admin123");
                admin.setRol(Usuario.Rol.ADMINISTRADOR);
                usuarioRepository.save(admin);
                System.out.println("✅ Admin creado");
            }

            // ── COORDINADOR ──────────────────────────────────────────────
            if (!coordinadorRepository.existsByCorreo("coordinador@saberpro.edu.co")) {
                Coordinador coord = new Coordinador();
                coord.setNumeroCedula("1000000002");
                coord.setPrimerNombre("Carlos");
                coord.setSegundoNombre("Andrés");
                coord.setPrimerApellido("Ramírez");
                coord.setSegundoApellido("Mora");
                coord.setCorreo("coordinador@saberpro.edu.co");
                coord.setTelefono("3101234567");
                coord.setAreaAsignada("Coordinación Académica");
                coord.setContrasena("coord123");
                coord.setRol(Usuario.Rol.COORDINADOR);
                coordinadorRepository.save(coord);
                System.out.println("✅ Coordinador creado");
            }

            // ── DOCENTE 1 — Tecnología ────────────────────────────────────
            if (!docenteRepository.existsByCorreo("docente1@saberpro.edu.co")) {
                Docente doc1 = new Docente();
                doc1.setNumeroCedula("1000000003");
                doc1.setPrimerNombre("María");
                doc1.setSegundoNombre("Fernanda");
                doc1.setPrimerApellido("González");
                doc1.setSegundoApellido("López");
                doc1.setCorreo("docente1@saberpro.edu.co");
                doc1.setTelefono("3119876543");
                doc1.setAreaAsignada("Sistemas Informáticos");
                doc1.setContrasena("doc1123");
                doc1.setRol(Usuario.Rol.DOCENTE);
                doc1.setFacultad(facTec);
                docenteRepository.save(doc1);
                System.out.println("✅ Docente 1 creado");
            }

            // ── DOCENTE 2 — Ingeniería ────────────────────────────────────
            if (!docenteRepository.existsByCorreo("docente2@saberpro.edu.co")) {
                Docente doc2 = new Docente();
                doc2.setNumeroCedula("1000000004");
                doc2.setPrimerNombre("Juan");
                doc2.setSegundoNombre("Pablo");
                doc2.setPrimerApellido("Mendez");
                doc2.setSegundoApellido("Ruiz");
                doc2.setCorreo("docente2@saberpro.edu.co");
                doc2.setTelefono("3187654321");
                doc2.setAreaAsignada("Ingeniería Industrial");
                doc2.setContrasena("doc2123");
                doc2.setRol(Usuario.Rol.DOCENTE);
                doc2.setFacultad(facIng);
                docenteRepository.save(doc2);
                System.out.println("✅ Docente 2 creado");
            }

            // ── ESTUDIANTES ──────────────────────────────────────────────
            // Tecnología — Sistemas Informáticos (12 estudiantes)
            crear(estudianteRepository, resultadoRepository,
                "1020183007722","BARBOSA","","CARLOS","","EK20183007722",
                "barbosa.ek20183007722@saberpro.edu.co","3000000001",
                "Sistemas Informáticos", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                200,"Nivel 4",128.0,"Nivel 2",182.0,"Nivel 3",202.0,"Nivel 4",
                206.0,"Nivel 4",183.0,"Nivel 3","B1",185.0,"Nivel 3",160.0,"Nivel 3",197.0,"Nivel 4");

            crear(estudianteRepository, resultadoRepository,
                "1020183140703","QUINTERO","","ANDREA","","EK20183140703",
                "quintero.ek20183140703@saberpro.edu.co","3000000002",
                "Sistemas Informáticos", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                165,"Nivel 3",125.0,"Nivel 1",151.0,"Nivel 2",179.0,"Nivel 3",
                163.0,"Nivel 3",205.0,"Nivel 4","B2",182.0,"Nivel 3",144.0,"Nivel 2",136.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183040545","PARRA","","MIGUEL","","EK20183040545",
                "parra.ek20183040545@saberpro.edu.co","3000000003",
                "Sistemas Informáticos", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 7,
                164,"Nivel 3",159.0,"Nivel 3",172.0,"Nivel 3",182.0,"Nivel 3",
                142.0,"Nivel 2",165.0,"Nivel 3","A2",167.0,"Nivel 3",132.0,"Nivel 2",148.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183025381","ANAYA","","LUCIA","","EK20183025381",
                "anaya.ek20183025381@saberpro.edu.co","3000000004",
                "Sistemas Informáticos", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                160,"Nivel 3",146.0,"Nivel 2",199.0,"Nivel 4",157.0,"Nivel 3",
                149.0,"Nivel 2",147.0,"Nivel 2","A2",174.0,"Nivel 3",127.0,"Nivel 2",171.0,"Nivel 3");

            crear(estudianteRepository, resultadoRepository,
                "1020183025335","FLOR","","JUAN","","EK20183025335",
                "flor.ek20183025335@saberpro.edu.co","3000000005",
                "Telecomunicaciones", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                160,"Nivel 3",198.0,"Nivel 4",153.0,"Nivel 2",147.0,"Nivel 2",
                157.0,"Nivel 3",146.0,"Nivel 2","A2",168.0,"Nivel 3",114.0,"Nivel 1",160.0,"Nivel 3");

            crear(estudianteRepository, resultadoRepository,
                "1020183122648","GARCIA","","PEDRO","","EK20183122648",
                "garcia.ek20183122648@saberpro.edu.co","3000000006",
                "Telecomunicaciones", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 7,
                157,"Nivel 3",179.0,"Nivel 3",172.0,"Nivel 3",158.0,"Nivel 3",
                140.0,"Nivel 2",136.0,"Nivel 2","A1",128.0,"Nivel 2",121.0,"Nivel 1",142.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183064605","MANOSALVA","","DIANA","","EK20183064605",
                "manosalva.ek20183064605@saberpro.edu.co","3000000007",
                "Telecomunicaciones", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                153,"Nivel 2",115.0,"Nivel 1",152.0,"Nivel 2",159.0,"Nivel 3",
                172.0,"Nivel 3",165.0,"Nivel 3","A2",142.0,"Nivel 2",118.0,"Nivel 1",119.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183187351","MENDOZA","","FELIPE","","EK20183187351",
                "mendoza.ek20183187351@saberpro.edu.co","3000000008",
                "Electromecanica", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                151,"Nivel 2",132.0,"Nivel 2",123.0,"Nivel 1",125.0,"Nivel 1",
                169.0,"Nivel 3",204.0,"Nivel 4","B2",173.0,"Nivel 3",127.0,"Nivel 2",171.0,"Nivel 3");

            crear(estudianteRepository, resultadoRepository,
                "1020183233820","BELTRAN","","SARA","","EK20183233820",
                "beltran.ek20183233820@saberpro.edu.co","3000000009",
                "Electromecanica", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 7,
                150,"Nivel 2",86.0,"Nivel 1",187.0,"Nivel 3",160.0,"Nivel 3",
                171.0,"Nivel 3",148.0,"Nivel 2","A2",162.0,"Nivel 3",125.0,"Nivel 1",142.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183030016","SANTAMARIA","","JOSE","","EK20183030016",
                "santamaria.ek20183030016@saberpro.edu.co","3000000010",
                "Electromecanica", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 8,
                150,"Nivel 2",175.0,"Nivel 3",149.0,"Nivel 2",145.0,"Nivel 2",
                158.0,"Nivel 3",125.0,"Nivel 1","A1",162.0,"Nivel 3",76.0,"Nivel 1",125.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183047073","SANCHEZ","","MARIA","","EK20183047073",
                "sanchez.ek20183047073@saberpro.edu.co","3000000011",
                "Sistemas Informáticos", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 6,
                149,"Nivel 2",209.0,"Nivel 4",143.0,"Nivel 2",117.0,"Nivel 1",
                129.0,"Nivel 2",147.0,"Nivel 2","A2",137.0,"Nivel 2",125.0,"Nivel 1",136.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183236451","ROMERO","","LUIS","","EK20183236451",
                "romero.ek20183236451@saberpro.edu.co","3000000012",
                "Telecomunicaciones", Estudiante.TipoPrograma.TECNOLOGIA, facTec, 7,
                146,"Nivel 2",93.0,"Nivel 1",183.0,"Nivel 3",155.0,"Nivel 2",
                164.0,"Nivel 3",133.0,"Nivel 2","A1",174.0,"Nivel 3",130.0,"Nivel 2",154.0,"Nivel 2");

            // Ingeniería — programas (22 estudiantes)
            crear(estudianteRepository, resultadoRepository,
                "1020183041714","LUNA","","ANA","","EK20183041714",
                "luna.ek20183041714@saberpro.edu.co","3000000013",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                141,"Nivel 2",125.0,"Nivel 1",157.0,"Nivel 3",138.0,"Nivel 2",
                135.0,"Nivel 2",152.0,"Nivel 2","A2",176.0,"Nivel 3",128.0,"Nivel 2",165.0,"Nivel 3");

            crear(estudianteRepository, resultadoRepository,
                "1020183187801","TRIANA","","DAVID","","EK20183187801",
                "triana.ek20183187801@saberpro.edu.co","3000000014",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 9,
                141,"Nivel 2",150.0,"Nivel 2",136.0,"Nivel 2",145.0,"Nivel 2",
                150.0,"Nivel 2",126.0,"Nivel 2","A1",148.0,"Nivel 2",129.0,"Nivel 2",131.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183176566","SUAREZ","","CAMILA","","EK20183176566",
                "suarez.ek20183176566@saberpro.edu.co","3000000015",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                140,"Nivel 2",128.0,"Nivel 2",146.0,"Nivel 2",146.0,"Nivel 2",
                132.0,"Nivel 2",147.0,"Nivel 2","A2",130.0,"Nivel 2",110.0,"Nivel 1",125.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183204427","GARCIA","","PABLO","","EK20183204427",
                "garcia2.ek20183204427@saberpro.edu.co","3000000016",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                139,"Nivel 2",129.0,"Nivel 2",138.0,"Nivel 2",148.0,"Nivel 2",
                146.0,"Nivel 2",135.0,"Nivel 2","A1",109.0,"Nivel 1",107.0,"Nivel 1",131.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183196280","PINZON","","VALENTINA","","EK20183196280",
                "pinzon.ek20183196280@saberpro.edu.co","3000000017",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 9,
                138,"Nivel 2",153.0,"Nivel 2",123.0,"Nivel 1",127.0,"Nivel 2",
                147.0,"Nivel 2",140.0,"Nivel 2","A1",145.0,"Nivel 2",143.0,"Nivel 2",160.0,"Nivel 3");

            crear(estudianteRepository, resultadoRepository,
                "1020183173799","JAIMES","","NICOLAS","","EK20183173799",
                "jaimes.ek20183173799@saberpro.edu.co","3000000018",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                137,"Nivel 2",166.0,"Nivel 3",157.0,"Nivel 3",124.0,"Nivel 1",
                100.0,"Nivel 1",140.0,"Nivel 2","A1",100.0,"Nivel 1",105.0,"Nivel 1",113.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183009565","NINO","","ISABELLA","","EK20183009565",
                "nino.ek20183009565@saberpro.edu.co","3000000019",
                "Ingeniería Electromecánica", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                134,"Nivel 2",165.0,"Nivel 3",137.0,"Nivel 2",136.0,"Nivel 2",
                118.0,"Nivel 1",116.0,"Nivel 1","A0",146.0,"Nivel 2",122.0,"Nivel 1",154.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183117756","FABIAN","","SEBASTIAN","","EK20183117756",
                "fabian.ek20183117756@saberpro.edu.co","3000000020",
                "Ingeniería Electromecánica", Estudiante.TipoPrograma.PROFESIONAL, facIng, 9,
                133,"Nivel 2",139.0,"Nivel 2",93.0,"Nivel 1",168.0,"Nivel 3",
                150.0,"Nivel 2",114.0,"Nivel 1","A0",102.0,"Nivel 1",123.0,"Nivel 1",94.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183044579","HERNANDEZ","","DANIEL","","EK20183044579",
                "hernandez.ek20183044579@saberpro.edu.co","3000000021",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                132,"Nivel 2",116.0,"Nivel 1",166.0,"Nivel 3",136.0,"Nivel 2",
                104.0,"Nivel 1",140.0,"Nivel 2","A1",158.0,"Nivel 3",125.0,"Nivel 1",154.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183045760","LARIOS","","NATALIA","","EK20183045760",
                "larios.ek20183045760@saberpro.edu.co","3000000022",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 7,
                131,"Nivel 2",149.0,"Nivel 2",123.0,"Nivel 1",129.0,"Nivel 2",
                121.0,"Nivel 1",131.0,"Nivel 2","A1",101.0,"Nivel 1",102.0,"Nivel 1",165.0,"Nivel 3");

            crear(estudianteRepository, resultadoRepository,
                "1020183034044","CALDERON","","MANUELA","","EK20183034044",
                "calderon.ek20183034044@saberpro.edu.co","3000000023",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                130,"Nivel 2",127.0,"Nivel 2",147.0,"Nivel 2",134.0,"Nivel 2",
                111.0,"Nivel 1",131.0,"Nivel 2","A1",65.0,"Nivel 1",112.0,"Nivel 1",94.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183041521","VILLARREAL","","JORGE","","EK20183041521",
                "villarreal.ek20183041521@saberpro.edu.co","3000000024",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 9,
                129,"Nivel 2",96.0,"Nivel 1",162.0,"Nivel 3",114.0,"Nivel 1",
                131.0,"Nivel 2",144.0,"Nivel 2","A1",122.0,"Nivel 1",112.0,"Nivel 1",131.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183027436","RESTREPO","","LAURA","","EK20183027436",
                "restrepo.ek20183027436@saberpro.edu.co","3000000025",
                "Ingeniería Electromecánica", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                126,"Nivel 2",81.0,"Nivel 1",134.0,"Nivel 2",126.0,"Nivel 2",
                149.0,"Nivel 2",139.0,"Nivel 2","A1",127.0,"Nivel 2",136.0,"Nivel 2",142.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183031592","CACERES","","ANDRES","","EK20183031592",
                "caceres.ek20183031592@saberpro.edu.co","3000000026",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                125,"Nivel 1",124.0,"Nivel 1",135.0,"Nivel 2",108.0,"Nivel 1",
                92.0,"Nivel 1",165.0,"Nivel 3","A2",132.0,"Nivel 2",104.0,"Nivel 1",131.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183004153","TABARES","","SOFIA","","EK20183004153",
                "tabares.ek20183004153@saberpro.edu.co","3000000027",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 7,
                124,"Nivel 1",131.0,"Nivel 2",131.0,"Nivel 2",107.0,"Nivel 1",
                88.0,"Nivel 1",162.0,"Nivel 3","A2",136.0,"Nivel 2",112.0,"Nivel 1",148.0,"Nivel 2");

            crear(estudianteRepository, resultadoRepository,
                "1020183030783","NARANJO","","ESTEBAN","","EK20183030783",
                "naranjo.ek20183030783@saberpro.edu.co","3000000028",
                "Ingeniería Electromecánica", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                122,"Nivel 1",166.0,"Nivel 3",113.0,"Nivel 1",113.0,"Nivel 1",
                112.0,"Nivel 1",106.0,"Nivel 1","A0",135.0,"Nivel 2",117.0,"Nivel 1",119.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183024754","PRADA","","VALERIA","","EK20183024754",
                "prada.ek20183024754@saberpro.edu.co","3000000029",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 9,
                122,"Nivel 1",119.0,"Nivel 1",125.0,"Nivel 1",137.0,"Nivel 2",
                107.0,"Nivel 1",123.0,"Nivel 1","A1",83.0,"Nivel 1",104.0,"Nivel 1",119.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183186200","VARGAS","","SAMUEL","","EK20183186200",
                "vargas.ek20183186200@saberpro.edu.co","3000000030",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                114,"Nivel 1",95.0,"Nivel 1",120.0,"Nivel 1",151.0,"Nivel 2",
                86.0,"Nivel 1",119.0,"Nivel 1","A0",149.0,"Nivel 2",103.0,"Nivel 1",119.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183182410","TORRES","","GABRIELA","","EK20183182410",
                "torres.ek20183182410@saberpro.edu.co","3000000031",
                "Ingeniería Electromecánica", Estudiante.TipoPrograma.PROFESIONAL, facIng, 7,
                113,"Nivel 1",109.0,"Nivel 1",105.0,"Nivel 1",104.0,"Nivel 1",
                103.0,"Nivel 1",142.0,"Nivel 2","A1",102.0,"Nivel 1",135.0,"Nivel 2",80.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183213735","ORTIZ","","MIGUEL","","EK20183213735",
                "ortiz.ek20183213735@saberpro.edu.co","3000000032",
                "Ingeniería de Sistemas", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                107,"Nivel 1",128.0,"Nivel 2",81.0,"Nivel 1",107.0,"Nivel 1",
                102.0,"Nivel 1",119.0,"Nivel 1","A0",130.0,"Nivel 2",111.0,"Nivel 1",125.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183065220","VILLAMIZAR","","KAREN","","EK20183065220",
                "villamizar.ek20183065220@saberpro.edu.co","3000000033",
                "Ingeniería Industrial", Estudiante.TipoPrograma.PROFESIONAL, facIng, 9,
                106,"Nivel 1",134.0,"Nivel 2",96.0,"Nivel 1",92.0,"Nivel 1",
                110.0,"Nivel 1",97.0,"Nivel 1","A0",83.0,"Nivel 1",107.0,"Nivel 1",119.0,"Nivel 1");

            crear(estudianteRepository, resultadoRepository,
                "1020183028123","RESTREPO","","ALEJANDRO","","EK20183028123",
                "restrepo2.ek20183028123@saberpro.edu.co","3000000034",
                "Ingeniería Electromecánica", Estudiante.TipoPrograma.PROFESIONAL, facIng, 8,
                96,"Nivel 1",0.0,"Nivel 1",117.0,"Nivel 1",122.0,"Nivel 1",
                105.0,"Nivel 1",137.0,"Nivel 2","A1",157.0,"Nivel 3",96.0,"Nivel 1",131.0,"Nivel 2");

            System.out.println("✅ Todos los datos cargados correctamente.");
        };
    }

    private void crear(
            EstudianteRepository eRepo,
            ResultadoRepository  rRepo,
            String cedula, String ap1, String ap2,
            String n1, String n2,
            String registro, String correo, String telefono,
            String programa, Estudiante.TipoPrograma tipo,
            Facultad facultad, int semestre,
            int puntaje, String nivel,
            double comEsc,    String comEscNiv,
            double razCuant,  String razCuantNiv,
            double lecCrit,   String lecCritNiv,
            double compCiu,   String compCiuNiv,
            double ingles,    String inglesNiv, String nivelMCER,
            double formProy,  String formProyNiv,
            double pensCient, String pensCientNiv,
            double disenoSoft,String disenoSoftNiv) {

        if (eRepo.existsByNumeroCedula(cedula)) return;

        Estudiante est = new Estudiante();
        est.setNumeroCedula(cedula);
        est.setPrimerApellido(ap1);
        est.setSegundoApellido(ap2.isBlank() ? null : ap2);
        est.setPrimerNombre(n1);
        est.setSegundoNombre(n2.isBlank() ? null : n2);
        est.setNumeroRegistro(registro);
        est.setCorreo(correo);
        est.setTelefono(telefono);
        est.setContrasena(cedula);
        est.setRol(Usuario.Rol.ESTUDIANTE);
        est.setSemestre(semestre);
        est.setPrograma(programa);
        est.setTipoPrograma(tipo);
        est.setPagoSaberPro(true);
        est.setComprobantePago("TRN-2026-" + registro);
     // Aprobación: TYT >= 80, Pro >= 120
        boolean aprobado = tipo == Estudiante.TipoPrograma.TECNOLOGIA
            ? puntaje >= 80
            : puntaje >= 120;
        est.setAprobadoSaberPro(aprobado);
        est = eRepo.save(est);

        ResultadoSaberPro r = new ResultadoSaberPro();
        r.setEstudiante(est);
        r.setNumeroRegistro(registro);
        r.setPuntajeTotal(puntaje);
        r.setPuntajeNivel(nivel);
        r.setComunicacionEscrita(comEsc);
        r.setComunicacionEscritaNivel(comEscNiv);
        r.setRazonamientoCuantitativo(razCuant);
        r.setRazonamientoCuantitativoNivel(razCuantNiv);
        r.setLecturaCritica(lecCrit);
        r.setLecturaCriticaNivel(lecCritNiv);
        r.setCompetenciasCiudadanas(compCiu);
        r.setCompetenciasCiudadanasNivel(compCiuNiv);
        r.setIngles(ingles);
        r.setInglesNivel(inglesNiv);
        r.setNivelIngles(nivelMCER);
        r.setFormulacionProyectos(formProy);
        r.setFormulacionProyectosNivel(formProyNiv);
        r.setPensamientoCientifico(pensCient);
        r.setPensamientoCientificoNivel(pensCientNiv);
        r.setDisenoSoftware(disenoSoft);
        r.setDisenoSoftwareNivel(disenoSoftNiv);

     // Beneficio según tipo de programa
        BeneficioCalculator.calcular(r, tipo);

        rRepo.save(r);
    }
}