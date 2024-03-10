<script lang="ts" type="module">
	import { qr } from '@svelte-put/qr/svg';

	const qrData = {
		data: 'https://github.com/georgegiosue/fisesms/releases/latest/download/fisesms.apk',
		logo: 'github.png',
		shape: 'circle',
	} as const;
</script>

<svg
    class="w-32 h-32"
	use:qr={qrData}
	alt="Fise SMS APK"
/>