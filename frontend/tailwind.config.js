/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eef4ff',
          100: '#d9e6ff',
          200: '#bcd3ff',
          300: '#8fb6ff',
          400: '#5b90ff',
          500: '#3468f5',
          600: '#1f4de0',
          700: '#183db3',
          800: '#173a91',
          900: '#132c66',
          950: '#0c1a3f',
        },
        secondary: {
          50: '#f2f7f7',
          100: '#dcebea',
          200: '#b8d6d4',
          300: '#8bb8b5',
          400: '#5d9793',
          500: '#417d79',
          600: '#316461',
          700: '#29504e',
          800: '#234140',
          900: '#1f3736',
        },
        success: {
          50: '#ecfdf3',
          500: '#12b76a',
          600: '#039855',
          700: '#027a48',
        },
        warning: {
          50: '#fffaeb',
          500: '#f79009',
          600: '#dc6803',
          700: '#b54708',
        },
        danger: {
          50: '#fef3f2',
          500: '#f04438',
          600: '#d92d20',
          700: '#b42318',
        },
      },
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        card: '0 1px 3px rgba(16, 24, 40, 0.08), 0 1px 2px rgba(16, 24, 40, 0.06)',
      },
    },
    screens: {
      sm: '640px',
      md: '768px',
      lg: '1024px',
      xl: '1280px',
      '2xl': '1536px',
    },
  },
  plugins: [],
}
