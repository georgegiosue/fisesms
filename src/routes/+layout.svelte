<script lang="ts">
	import { dev } from '$app/environment';
	import Navbar from '$lib/components/Navbar.svelte';
	import { usePathname } from '$lib/utils.js';
	import { inject } from '@vercel/analytics';
	import { ModeWatcher } from 'mode-watcher';
	import { onMount } from 'svelte';
	import '../app.pcss';

	inject({ mode: dev ? 'development' : 'production' });

	onMount(() => {
		function handleKeydown(e: KeyboardEvent) {
			e.preventDefault();
			const key = e.key;

			const { hostname, protocol, port } = usePathname();
			const baseUrl = `${protocol}//${hostname}${port}`;

			switch (key) {
				case 'p':
				case 'P':
					window.location.href = `${baseUrl}/privacy`;
					break;
				case 'a':
				case 'A':
					window.location.href = `${baseUrl}/faq`;
					break;

				case 'h':
				case 'H':
					window.location.href = `${baseUrl}/`;
					break;
			}
		}

		document.addEventListener('keydown', handleKeydown);
		return () => {
			document.removeEventListener('keydown', handleKeydown);
		};
	});
</script>

<ModeWatcher />

<svelte:head>
	<title>Fise SMS</title>
	<meta
		name="description"
		content="Aplicación de Android que permite a los proveedores de GLP (Gas Licuado de Petróleo) procesar cupones de descuento"
	/>
</svelte:head>

<main class="m-2 rounded-lg p-4 lg:m-24 lg:mx-48">
	<Navbar />
	<section class="py-4">
		<slot />
	</section>
</main>
