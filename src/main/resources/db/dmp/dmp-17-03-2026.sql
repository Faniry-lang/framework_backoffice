--
-- PostgreSQL database dump
--

\restrict sUdmdiS5SxZBT8C5abcXVf4NK0jb3mhFtCvyxZC35RCKWOBTlOqqcW1UVA5OqSg

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: distance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.distance (
    id integer NOT NULL,
    code_from character varying(10),
    code_to character varying(10),
    distance_km numeric(10,2)
);


ALTER TABLE public.distance OWNER TO postgres;

--
-- Name: distance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.distance_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.distance_id_seq OWNER TO postgres;

--
-- Name: distance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.distance_id_seq OWNED BY public.distance.id;


--
-- Name: hotel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hotel (
    id integer NOT NULL,
    nom character varying,
    code character varying(10),
    aeroport boolean DEFAULT false
);


ALTER TABLE public.hotel OWNER TO postgres;

--
-- Name: hotel_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hotel_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.hotel_id_seq OWNER TO postgres;

--
-- Name: hotel_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.hotel_id_seq OWNED BY public.hotel.id;


--
-- Name: ordre_depart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ordre_depart (
    id character varying(50) NOT NULL,
    id_reservation integer NOT NULL,
    date_depart timestamp without time zone
);


ALTER TABLE public.ordre_depart OWNER TO postgres;

--
-- Name: reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reservation (
    id integer NOT NULL,
    nb_passager integer,
    id_client character varying(5),
    id_hotel integer,
    date_heure_arrivee timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    temps_attente_max integer DEFAULT 30
);


ALTER TABLE public.reservation OWNER TO postgres;

--
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reservation_id_seq OWNER TO postgres;

--
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reservation_id_seq OWNED BY public.reservation.id;


--
-- Name: token_client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token_client (
    id integer NOT NULL,
    token text,
    expiration_date timestamp without time zone
);


ALTER TABLE public.token_client OWNER TO postgres;

--
-- Name: token_client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.token_client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.token_client_id_seq OWNER TO postgres;

--
-- Name: token_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.token_client_id_seq OWNED BY public.token_client.id;


--
-- Name: trajet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trajet (
    id integer NOT NULL,
    id_vehicule integer NOT NULL,
    date_trajet date,
    heure_depart timestamp without time zone,
    heure_arrivee timestamp without time zone,
    distance_totale numeric(10,2),
    ordre_visites text
);


ALTER TABLE public.trajet OWNER TO postgres;

--
-- Name: trajet_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trajet_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trajet_id_seq OWNER TO postgres;

--
-- Name: trajet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trajet_id_seq OWNED BY public.trajet.id;


--
-- Name: trajet_reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trajet_reservation (
    id integer NOT NULL,
    id_trajet integer,
    id_reservation integer,
    ordre_visite integer
);


ALTER TABLE public.trajet_reservation OWNER TO postgres;

--
-- Name: trajet_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trajet_reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trajet_reservation_id_seq OWNER TO postgres;

--
-- Name: trajet_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trajet_reservation_id_seq OWNED BY public.trajet_reservation.id;


--
-- Name: vehicule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vehicule (
    id integer NOT NULL,
    ref character varying(10),
    nbr_place integer,
    type_carburant character varying(50),
    vitesse_moyenne numeric(5,2) DEFAULT 60.0
);


ALTER TABLE public.vehicule OWNER TO postgres;

--
-- Name: vehicule_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vehicule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vehicule_id_seq OWNER TO postgres;

--
-- Name: vehicule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.vehicule_id_seq OWNED BY public.vehicule.id;


--
-- Name: distance id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distance ALTER COLUMN id SET DEFAULT nextval('public.distance_id_seq'::regclass);


--
-- Name: hotel id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hotel ALTER COLUMN id SET DEFAULT nextval('public.hotel_id_seq'::regclass);


--
-- Name: reservation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation ALTER COLUMN id SET DEFAULT nextval('public.reservation_id_seq'::regclass);


--
-- Name: token_client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_client ALTER COLUMN id SET DEFAULT nextval('public.token_client_id_seq'::regclass);


--
-- Name: trajet id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet ALTER COLUMN id SET DEFAULT nextval('public.trajet_id_seq'::regclass);


--
-- Name: trajet_reservation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet_reservation ALTER COLUMN id SET DEFAULT nextval('public.trajet_reservation_id_seq'::regclass);


--
-- Name: vehicule id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicule ALTER COLUMN id SET DEFAULT nextval('public.vehicule_id_seq'::regclass);


--
-- Data for Name: distance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.distance (id, code_from, code_to, distance_km) FROM stdin;
2	AEROPORT	HOTEL-1	50.00
\.


--
-- Data for Name: hotel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hotel (id, nom, code, aeroport) FROM stdin;
1	Ivato	AEROPORT	t
2	Hotel1	HOTEL-1	f
\.


--
-- Data for Name: ordre_depart; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ordre_depart (id, id_reservation, date_depart) FROM stdin;
\.


--
-- Data for Name: reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) FROM stdin;
15	5	CLI1	2	2026-03-17 07:00:00	30
16	3	CLI2	2	2026-03-17 08:20:00	30
\.


--
-- Data for Name: token_client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.token_client (id, token, expiration_date) FROM stdin;
1	9eab49b4-aeb0-4c7b-b43b-2b9ab2258a36	2026-02-13 13:53:53.004267
\.


--
-- Data for Name: trajet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trajet (id, id_vehicule, date_trajet, heure_depart, heure_arrivee, distance_totale, ordre_visites) FROM stdin;
59	5	2026-03-17	2026-03-17 07:00:00	2026-03-17 08:40:00	100.00	AEROPORT,HOTEL-1,AEROPORT
60	5	2026-03-17	2026-03-17 08:40:00	2026-03-17 10:20:00	100.00	AEROPORT,HOTEL-1,AEROPORT
\.


--
-- Data for Name: trajet_reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trajet_reservation (id, id_trajet, id_reservation, ordre_visite) FROM stdin;
96	59	15	1
97	60	16	1
\.


--
-- Data for Name: vehicule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) FROM stdin;
5	VH-001	6	D	60.00
\.


--
-- Name: distance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.distance_id_seq', 2, true);


--
-- Name: hotel_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hotel_id_seq', 2, true);


--
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reservation_id_seq', 16, true);


--
-- Name: token_client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.token_client_id_seq', 1, true);


--
-- Name: trajet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trajet_id_seq', 60, true);


--
-- Name: trajet_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trajet_reservation_id_seq', 97, true);


--
-- Name: vehicule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vehicule_id_seq', 5, true);


--
-- Name: distance distance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distance
    ADD CONSTRAINT distance_pkey PRIMARY KEY (id);


--
-- Name: hotel hotel_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT hotel_code_key UNIQUE (code);


--
-- Name: hotel hotel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT hotel_pkey PRIMARY KEY (id);


--
-- Name: ordre_depart ordre_depart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordre_depart
    ADD CONSTRAINT ordre_depart_pkey PRIMARY KEY (id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: token_client token_client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_client
    ADD CONSTRAINT token_client_pkey PRIMARY KEY (id);


--
-- Name: trajet trajet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet
    ADD CONSTRAINT trajet_pkey PRIMARY KEY (id);


--
-- Name: trajet_reservation trajet_reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet_reservation
    ADD CONSTRAINT trajet_reservation_pkey PRIMARY KEY (id);


--
-- Name: vehicule vehicule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicule
    ADD CONSTRAINT vehicule_pkey PRIMARY KEY (id);


--
-- Name: code_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX code_index ON public.hotel USING btree (code);


--
-- Name: distance_from_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX distance_from_index ON public.distance USING btree (code_from);


--
-- Name: distance_to_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX distance_to_index ON public.distance USING btree (code_to);


--
-- Name: idx_vehicule_ref; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_vehicule_ref ON public.vehicule USING btree (ref);


--
-- Name: trajet_date_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trajet_date_index ON public.trajet USING btree (date_trajet);


--
-- Name: trajet_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trajet_index ON public.trajet_reservation USING btree (id_trajet);


--
-- Name: trajet_res_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trajet_res_index ON public.trajet_reservation USING btree (id_reservation);


--
-- Name: trajet_vehicule_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trajet_vehicule_index ON public.trajet USING btree (id_vehicule);


--
-- Name: reservation fk_reservation_hotel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fk_reservation_hotel FOREIGN KEY (id_hotel) REFERENCES public.hotel(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: ordre_depart ordre_depart_id_reservation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordre_depart
    ADD CONSTRAINT ordre_depart_id_reservation_fkey FOREIGN KEY (id_reservation) REFERENCES public.reservation(id);


--
-- Name: trajet trajet_id_vehicule_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet
    ADD CONSTRAINT trajet_id_vehicule_fkey FOREIGN KEY (id_vehicule) REFERENCES public.vehicule(id);


--
-- Name: trajet_reservation trajet_reservation_id_reservation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet_reservation
    ADD CONSTRAINT trajet_reservation_id_reservation_fkey FOREIGN KEY (id_reservation) REFERENCES public.reservation(id);


--
-- Name: trajet_reservation trajet_reservation_id_trajet_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet_reservation
    ADD CONSTRAINT trajet_reservation_id_trajet_fkey FOREIGN KEY (id_trajet) REFERENCES public.trajet(id);


--
-- PostgreSQL database dump complete
--

\unrestrict sUdmdiS5SxZBT8C5abcXVf4NK0jb3mhFtCvyxZC35RCKWOBTlOqqcW1UVA5OqSg

