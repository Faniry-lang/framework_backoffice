--
-- PostgreSQL database dump
--

\restrict rK3lfbgwETJIgawb4QiGVEAVlDBUNeAZvCxccxa5Gi735uBPezvX1PWB2IvsPQ9

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
    ordre_visite integer,
    nbr_passager integer
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
    vitesse_moyenne numeric(5,2) DEFAULT 60.0,
    heure_dispo character varying
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

INSERT INTO public.distance VALUES (1, 'APT', 'IBIS', 75.00);
INSERT INTO public.distance VALUES (2, 'APT', 'NOVO', 60.00);
INSERT INTO public.distance VALUES (3, 'IBIS', 'NOVO', 43.00);


--
-- Data for Name: hotel; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.hotel VALUES (1, 'Aeroport', 'APT', true);
INSERT INTO public.hotel VALUES (2, 'Ibis', 'IBIS', false);
INSERT INTO public.hotel VALUES (3, 'Novotel', 'NOVO', false);


--
-- Data for Name: ordre_depart; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.reservation VALUES (5, 6, 'R1', 2, '2026-03-19 08:00:00', 30);
INSERT INTO public.reservation VALUES (6, 4, 'R2', 2, '2026-03-19 08:10:00', 30);
INSERT INTO public.reservation VALUES (7, 3, 'R3', 2, '2026-03-19 08:15:00', 30);


--
-- Data for Name: token_client; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: trajet; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.trajet VALUES (8, 1, '2026-03-19', '2026-03-19 08:00:00', '2026-03-19 13:00:00', 150.00, 'APT,IBIS,APT');


--
-- Data for Name: trajet_reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.trajet_reservation VALUES (8, 8, 5, 1, 6);


--
-- Data for Name: vehicule; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.vehicule VALUES (1, 'V1', 8, 'D', 30.00, '00:00');
INSERT INTO public.vehicule VALUES (2, 'V3', 5, 'D', 30.00, '23:00');
INSERT INTO public.vehicule VALUES (3, 'V2', 3, 'ES', 30.00, '23:00');


--
-- Name: distance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.distance_id_seq', 3, true);


--
-- Name: hotel_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hotel_id_seq', 3, true);


--
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reservation_id_seq', 7, true);


--
-- Name: token_client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.token_client_id_seq', 1, false);


--
-- Name: trajet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trajet_id_seq', 8, true);


--
-- Name: trajet_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trajet_reservation_id_seq', 8, true);


--
-- Name: vehicule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vehicule_id_seq', 3, true);


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

\unrestrict rK3lfbgwETJIgawb4QiGVEAVlDBUNeAZvCxccxa5Gi735uBPezvX1PWB2IvsPQ9

